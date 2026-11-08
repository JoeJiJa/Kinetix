package dev.anilbeesetti.nextplayer.feature.player.ui

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import dev.anilbeesetti.nextplayer.core.ui.R
import dev.anilbeesetti.nextplayer.core.ui.components.GlassSurface
import dev.anilbeesetti.nextplayer.core.ui.components.MochyGlassBackground
import dev.anilbeesetti.nextplayer.core.ui.components.MochyGlassSpecularBorderBrush
import dev.anilbeesetti.nextplayer.core.ui.components.NextOutlinedTextField
import dev.anilbeesetti.nextplayer.core.ui.components.bouncyPress
import dev.anilbeesetti.nextplayer.core.ui.designsystem.NextIcons
import dev.anilbeesetti.nextplayer.feature.player.OnlineSubtitleDownloader
import dev.anilbeesetti.nextplayer.feature.player.OnlineSubtitleItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val SUPPORTED_LANGUAGES = listOf(
    "eng" to "English",
    "tam" to "Tamil",
    "hin" to "Hindi",
    "spa" to "Spanish",
    "fre" to "French",
)

/**
 * Cleans video title by stripping file extension and resolution tags.
 */
fun cleanVideoTitle(rawTitle: String): String {
    var cleaned = rawTitle
    // Remove extension
    val dotIndex = cleaned.lastIndexOf('.')
    if (dotIndex > 0) {
        cleaned = cleaned.substring(0, dotIndex)
    }
    // Remove common resolution & quality patterns
    val qualityRegex = Regex(
        "(?i)[._ -]*(1080p|720p|480p|2160p|4k|uhd|bluray|bdrip|brrip|web-dl|webrip|dvdrip|x264|x265|hevc|h264|10bit|aac|dts)[._ -]*.*",
    )
    cleaned = cleaned.replace(qualityRegex, "")
    // Replace dots and underscores with spaces
    cleaned = cleaned.replace(Regex("[._]+"), " ")
    return cleaned.trim()
}

@Composable
fun OnlineSubtitleSearchDialog(
    initialTitle: String,
    onDismissRequest: () -> Unit,
    onSubtitleApplied: (android.net.Uri) -> Unit,
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()

    var searchQuery by remember { mutableStateOf(cleanVideoTitle(initialTitle)) }
    var selectedLanguage by remember { mutableStateOf("eng") }
    var isSearching by remember { mutableStateOf(false) }
    var isDownloading by remember { mutableStateOf(false) }
    var results by remember { mutableStateOf<List<OnlineSubtitleItem>?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun performSearch() {
        if (searchQuery.isBlank() || isSearching) return
        keyboardController?.hide()
        isSearching = true
        errorMessage = null
        coroutineScope.launch {
            try {
                val list = OnlineSubtitleDownloader.searchSubtitles(
                    query = searchQuery,
                    lang = selectedLanguage,
                )
                results = list
                if (list.isEmpty()) {
                    errorMessage = context.getString(R.string.no_subtitles_found)
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: context.getString(R.string.no_subtitles_found)
                results = emptyList()
            } finally {
                isSearching = false
            }
        }
    }

    Dialog(
        onDismissRequest = {
            if (!isDownloading) onDismissRequest()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = !isDownloading,
            dismissOnClickOutside = !isDownloading,
        ),
    ) {
        GlassSurface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .heightIn(min = 280.dp, max = 560.dp),
            shape = RoundedCornerShape(24.dp),
            backgroundColor = MochyGlassBackground,
            borderBrush = MochyGlassSpecularBorderBrush,
            borderWidth = 1.dp,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.search_subtitles_online),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                    IconButton(
                        onClick = onDismissRequest,
                        enabled = !isDownloading,
                    ) {
                        Icon(
                            imageVector = NextIcons.Close,
                            contentDescription = stringResource(R.string.cancel),
                            tint = Color.White.copy(alpha = 0.8f),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Search Input & Action Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    NextOutlinedTextField(
                        modifier = Modifier.weight(1f),
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text(
                                text = stringResource(R.string.search_subtitles_query_hint),
                                color = Color.White.copy(alpha = 0.45f),
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { performSearch() }),
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    val searchButtonInteractionSource = remember { MutableInteractionSource() }
                    Button(
                        modifier = Modifier
                            .height(52.dp)
                            .bouncyPress(interactionSource = searchButtonInteractionSource),
                        interactionSource = searchButtonInteractionSource,
                        onClick = { performSearch() },
                        enabled = !isSearching && searchQuery.isNotBlank(),
                        shape = RoundedCornerShape(14.dp),
                    ) {
                        Text(text = stringResource(R.string.search))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Language Chip Selector
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(SUPPORTED_LANGUAGES) { (code, label) ->
                        FilterChip(
                            selected = selectedLanguage == code,
                            onClick = {
                                selectedLanguage = code
                                performSearch()
                            },
                            label = { Text(text = label) },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color.White.copy(alpha = 0.08f),
                                labelColor = Color.White.copy(alpha = 0.8f),
                                selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f),
                                selectedLabelColor = MaterialTheme.colorScheme.primary,
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selectedLanguage == code,
                                borderColor = Color.White.copy(alpha = 0.15f),
                                selectedBorderColor = MaterialTheme.colorScheme.primary,
                            ),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Results / States
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false),
                    contentAlignment = Alignment.Center,
                ) {
                    when {
                        isSearching -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(24.dp),
                            ) {
                                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = stringResource(R.string.searching_subtitles),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White.copy(alpha = 0.7f),
                                )
                            }
                        }

                        isDownloading -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(24.dp),
                            ) {
                                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = stringResource(R.string.downloading_subtitle),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White.copy(alpha = 0.7f),
                                )
                            }
                        }

                        errorMessage != null -> {
                            Text(
                                text = errorMessage.orEmpty(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.padding(24.dp),
                            )
                        }

                        results != null -> {
                            val subList = results.orEmpty()
                            if (subList.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.no_subtitles_found),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White.copy(alpha = 0.7f),
                                    modifier = Modifier.padding(24.dp),
                                )
                            } else {
                                LazyColumn(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    items(subList) { item ->
                                        SubtitleItemRow(
                                            item = item,
                                            onItemClick = {
                                                if (isDownloading) return@SubtitleItemRow
                                                isDownloading = true
                                                coroutineScope.launch {
                                                    val uri = OnlineSubtitleDownloader.downloadSubtitle(
                                                        item = item,
                                                        cacheDir = context.cacheDir,
                                                    )
                                                    withContext(Dispatchers.Main) {
                                                        isDownloading = false
                                                        if (uri != null) {
                                                            Toast.makeText(
                                                                context,
                                                                R.string.subtitle_applied_successfully,
                                                                Toast.LENGTH_SHORT,
                                                            ).show()
                                                            onSubtitleApplied(uri)
                                                            onDismissRequest()
                                                        } else {
                                                            Toast.makeText(
                                                                context,
                                                                R.string.failed_to_download_subtitle,
                                                                Toast.LENGTH_SHORT,
                                                            ).show()
                                                        }
                                                    }
                                                }
                                            },
                                        )
                                    }
                                }
                            }
                        }

                        else -> {
                            Text(
                                text = stringResource(R.string.search_subtitles_query_hint),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.5f),
                                modifier = Modifier.padding(24.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SubtitleItemRow(
    item: OnlineSubtitleItem,
    onItemClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    GlassSurface(
        modifier = Modifier
            .fillMaxWidth()
            .bouncyPress(interactionSource = interactionSource)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onItemClick,
            ),
        shape = RoundedCornerShape(14.dp),
        backgroundColor = Color.White.copy(alpha = 0.05f),
        borderBrush = MochyGlassSpecularBorderBrush,
        borderWidth = 1.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.language.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "• ${item.format.uppercase()}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.6f),
                    )
                    if (item.hearingImpaired) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "• HI",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFFFD54F),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = NextIcons.ArrowDownward,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.6f),
            )
        }
    }
}
