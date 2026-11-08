package dev.anilbeesetti.nextplayer.feature.player

import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.zip.GZIPInputStream

data class OnlineSubtitleItem(
    val title: String,
    val language: String,
    val downloadUrl: String,
    val format: String = "srt",
    val hearingImpaired: Boolean = false,
)

object OnlineSubtitleDownloader {

    private const val CONNECT_TIMEOUT_MS = 10_000
    private const val READ_TIMEOUT_MS = 15_000

    suspend fun searchSubtitles(
        query: String,
        lang: String = "eng",
    ): List<OnlineSubtitleItem> = withContext(Dispatchers.IO) {
        val trimmedQuery = query.trim()
        if (trimmedQuery.isEmpty()) return@withContext emptyList()

        val results = mutableListOf<OnlineSubtitleItem>()

        // 1. Try OpenSubtitles REST endpoint
        try {
            val encodedQuery = URLEncoder.encode(trimmedQuery, "UTF-8")
            val openSubUrl = "https://rest.opensubtitles.org/search/query-$encodedQuery/sublanguageid-$lang"
            val conn = (URL(openSubUrl).openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                setRequestProperty("User-Agent", "TemporaryUserAgent")
                connectTimeout = CONNECT_TIMEOUT_MS
                readTimeout = READ_TIMEOUT_MS
            }

            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                val responseBody = conn.inputStream.bufferedReader().use { it.readText() }
                val jsonArray = JSONArray(responseBody)
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.optJSONObject(i) ?: continue
                    val subDownloadLink = obj.optString("SubDownloadLink")
                    val subFileName = obj.optString("SubFileName").ifEmpty {
                        obj.optString("MovieReleaseName")
                    }
                    val subFormat = obj.optString("SubFormat").ifEmpty { "srt" }
                    val subLanguage = obj.optString("SubLanguageID", lang)
                    val hearingImpaired = obj.optInt("SubHearingImpaired", 0) == 1

                    if (subDownloadLink.isNotEmpty()) {
                        results.add(
                            OnlineSubtitleItem(
                                title = subFileName.ifEmpty { "Subtitle ${i + 1}" },
                                language = subLanguage,
                                downloadUrl = subDownloadLink,
                                format = subFormat,
                                hearingImpaired = hearingImpaired,
                            ),
                        )
                    }
                }
            }
        } catch (_: Exception) {
            // Silently fall back to Podnapisi
        }

        // 2. Fallback to Podnapisi JSON API if no results or error
        if (results.isEmpty()) {
            try {
                val encodedQuery = URLEncoder.encode(trimmedQuery, "UTF-8")
                val podnapisiUrl = "https://www.podnapisi.net/subtitles/search/advanced?keywords=$encodedQuery&language=$lang"
                val conn = (URL(podnapisiUrl).openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    setRequestProperty("Accept", "application/json")
                    setRequestProperty("User-Agent", "Mozilla/5.0")
                    connectTimeout = CONNECT_TIMEOUT_MS
                    readTimeout = READ_TIMEOUT_MS
                }

                if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                    val responseBody = conn.inputStream.bufferedReader().use { it.readText() }
                    val root = JSONObject(responseBody)
                    val dataArray = root.optJSONArray("data")
                    if (dataArray != null) {
                        for (i in 0 until dataArray.length()) {
                            val item = dataArray.optJSONObject(i) ?: continue
                            val title = item.optString("release").ifEmpty {
                                item.optString("title")
                            }
                            val downloadPath = item.optString("url")
                            val fullUrl = if (downloadPath.startsWith("http")) {
                                downloadPath
                            } else {
                                "https://www.podnapisi.net$downloadPath/download"
                            }
                            val subLang = item.optString("language", lang)
                            val hearingImpaired = item.optBoolean("hearing_impaired", false)

                            if (downloadPath.isNotEmpty()) {
                                results.add(
                                    OnlineSubtitleItem(
                                        title = title.ifEmpty { "Subtitle ${i + 1}" },
                                        language = subLang,
                                        downloadUrl = fullUrl,
                                        format = "srt",
                                        hearingImpaired = hearingImpaired,
                                    ),
                                )
                            }
                        }
                    }
                }
            } catch (_: Exception) {
                // Return whatever collected or empty
            }
        }

        results
    }

    suspend fun downloadSubtitle(
        item: OnlineSubtitleItem,
        cacheDir: File,
    ): Uri? = withContext(Dispatchers.IO) {
        try {
            val subsDir = File(cacheDir, "online_subs").apply { if (!exists()) mkdirs() }
            val sanitizedName = item.title
                .replace(Regex("[^a-zA-Z0-9._-]"), "_")
                .take(64)
            val extension = if (item.format.isNotBlank()) item.format else "srt"
            val targetFile = File(subsDir, "${sanitizedName}_${System.currentTimeMillis()}.$extension")

            val conn = (URL(item.downloadUrl).openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                setRequestProperty("User-Agent", "TemporaryUserAgent")
                instanceFollowRedirects = true
                connectTimeout = CONNECT_TIMEOUT_MS
                readTimeout = READ_TIMEOUT_MS
            }

            if (conn.responseCode !in 200..299) {
                return@withContext null
            }

            val isGzip = item.downloadUrl.endsWith(".gz", ignoreCase = true) ||
                "gzip".equals(conn.contentEncoding, ignoreCase = true)

            var inputStream: InputStream = conn.inputStream
            if (isGzip) {
                inputStream = GZIPInputStream(inputStream)
            }

            FileOutputStream(targetFile).use { out ->
                inputStream.use { input ->
                    input.copyTo(out)
                }
            }

            Uri.fromFile(targetFile)
        } catch (_: Exception) {
            null
        }
    }
}
