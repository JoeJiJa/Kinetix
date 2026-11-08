![Kinetix banner](fastlane/metadata/android/en-US/images/featureGraphic.png)

# Kinetix

[![GitHub release (latest SemVer)](https://img.shields.io/github/v/release/anilbeesetti/nextplayer.svg?logo=github&label=GitHub&cacheSeconds=3600)](https://github.com/anilbeesetti/nextplayer/releases/latest)
[![Google Play](https://img.shields.io/endpoint?color=green&logo=google-play&url=https%3A%2F%2Fplay.cuzi.workers.dev%2Fplay%3Fi%3Ddev.anilbeesetti.nextplayer%26l%3DGoogle%2520Play%26m%3Dv%24version)](https://play.google.com/store/apps/details?id=dev.anilbeesetti.nextplayer)
[![IzzyOnDroid](https://img.shields.io/endpoint?url=https://apt.izzysoft.de/fdroid/api/v1/shield/dev.anilbeesetti.nextplayer)](https://apt.izzysoft.de/fdroid/index/apk/dev.anilbeesetti.nextplayer)
[![F-Droid](https://img.shields.io/f-droid/v/dev.anilbeesetti.nextplayer?logo=f-droid&label=F-Droid&cacheSeconds=3600)](https://f-droid.org/packages/dev.anilbeesetti.nextplayer)
[![GitHub all releases](https://img.shields.io/github/downloads/anilbeesetti/nextplayer/total?logo=github&cacheSeconds=3600)](https://github.com/anilbeesetti/nextplayer/releases/latest)
[![Google Play](https://img.shields.io/endpoint?color=green&logo=google-play&url=https%3A%2F%2Fplay.cuzi.workers.dev%2Fplay%3Fi%3Ddev.anilbeesetti.nextplayer%26l%3Ddownloads%26m%3D%24totalinstalls)](https://play.google.com/store/apps/details?id=dev.anilbeesetti.nextplayer)
[![Weblate project translated](https://img.shields.io/weblate/progress/next-player?logo=weblate&logoColor=white&cacheSeconds=36000)](https://hosted.weblate.org/engage/next-player/)

**Kinetix** is a next-gen, glassmorphic Media3 video player for Android written in Kotlin and Jetpack Compose. Engineered for smooth performance and immersive viewing, Kinetix pairs modern design aesthetics with advanced playback controls and media indexing.

**This project is actively maintained. Please report any issues or feature requests in the [Issues](https://github.com/anilbeesetti/nextplayer/issues) section.**

[<img src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" alt="Get it on Google Play" height="80"/>](https://play.google.com/store/apps/details?id=dev.anilbeesetti.nextplayer)
[<img src="https://gitlab.com/IzzyOnDroid/repo/-/raw/master/assets/IzzyOnDroid.png" alt="Get it on IzzyOnDroid" height="80">](https://apt.izzysoft.de/fdroid/index/apk/dev.anilbeesetti.nextplayer)
[<img src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png" alt="Get it on F-Droid" height="80">](https://f-droid.org/packages/dev.anilbeesetti.nextplayer/)

> For GitHub releases, you can verify the app with the SHA-256 fingerprint certificate: `D3:7C:AB:F3:67:6C:F5:82:24:FB:24:2C:8D:D2:09:12:9D:05:88:03:79:29:5F:AA:A6:D0:FB:60:7B:EC:13:1B`

## Key Highlights

- **Global/Per-Video Persistent Playback Speed**: Configure speed globally or remember playback pace individually per video.
- **Long-Press + Horizontal Drag Dynamic Speed Scrubbing**: Seamlessly scrub playback speed dynamically on the fly with fluid touch gestures.
- **Built-in Online Subtitle Search & Auto-Attach**: Search online subtitles directly inside the player and automatically attach matching tracks.
- **Animated Glassmorphic Jetpack Compose UI**: Beautiful, modern translucent interfaces crafted with smooth Compose animations and Material You dynamic themes.
- **High-Speed Batch MediaStore Indexing**: Rapid discovery and categorization of local video libraries and folders with minimal battery and memory overhead.

## Screenshots

### Phone

<div style="width:100%; display:flex; justify-content:space-between; flex-wrap:wrap; gap:8px;">

[<img src="fastlane/metadata/android/en-US/images/phoneScreenshots/1.png" width=19% alt="Home folders">](fastlane/metadata/android/en-US/images/phoneScreenshots/1.png)
[<img src="fastlane/metadata/android/en-US/images/phoneScreenshots/2.png" width=19% alt="Demo folder videos">](fastlane/metadata/android/en-US/images/phoneScreenshots/2.png)
[<img src="fastlane/metadata/android/en-US/images/phoneScreenshots/3.png" width=19% alt="Quick settings">](fastlane/metadata/android/en-US/images/phoneScreenshots/3.png)
[<img src="fastlane/metadata/android/en-US/images/phoneScreenshots/4.png" width=19% alt="Grid view">](fastlane/metadata/android/en-US/images/phoneScreenshots/4.png)
[<img src="fastlane/metadata/android/en-US/images/phoneScreenshots/6.png" width=19% alt="Search results">](fastlane/metadata/android/en-US/images/phoneScreenshots/6.png)
[<img src="fastlane/metadata/android/en-US/images/phoneScreenshots/7.png" width=19% alt="Add network connection">](fastlane/metadata/android/en-US/images/phoneScreenshots/7.png)
[<img src="fastlane/metadata/android/en-US/images/phoneScreenshots/8.png" width=19% alt="Appearance settings">](fastlane/metadata/android/en-US/images/phoneScreenshots/8.png)
</div>

### Player UI

<div style="width:100%; display:flex; justify-content:space-between;">

[<img src="fastlane/metadata/android/en-US/images/phoneScreenshots/5.png" width=49% alt="Phone player controls">](fastlane/metadata/android/en-US/images/phoneScreenshots/5.png)
[<img src="fastlane/metadata/android/en-US/images/tvScreenshots/3.png" width=49% alt="TV player controls">](fastlane/metadata/android/en-US/images/tvScreenshots/3.png)
</div>

### Android TV

<div style="width:100%; display:flex; justify-content:space-between; flex-wrap:wrap; gap:8px;">

[<img src="fastlane/metadata/android/en-US/images/tvScreenshots/1.png" width=49% alt="TV home folders">](fastlane/metadata/android/en-US/images/tvScreenshots/1.png)
[<img src="fastlane/metadata/android/en-US/images/tvScreenshots/2.png" width=49% alt="TV demo folder videos">](fastlane/metadata/android/en-US/images/tvScreenshots/2.png)
[<img src="fastlane/metadata/android/en-US/images/tvScreenshots/4.png" width=49% alt="TV appearance settings">](fastlane/metadata/android/en-US/images/tvScreenshots/4.png)
</div>

## Player intent API

Kinetix supports MX Player-compatible VIEW intents, with additional playback
speed and direct background-play options. See the [Player intent API reference](PLAYER_API.md)
for integration examples, supported extras, URI permissions, playback results,
and compatibility details.

## Supported formats

- **Video**: H.263, H.264 AVC (Baseline Profile; Main Profile on Android 6+), H.265 HEVC, MPEG-4 SP, VP8, VP9, AV1
    - Support depends on Android device
- **Audio**: Vorbis, Opus, FLAC, ALAC, PCM/WAVE (μ-law, A-law), MP1, MP2, MP3, AMR (NB, WB), AAC (LC, ELD, HE; xHE on Android 9+), AC-3, E-AC-3, DTS, DTS-HD, TrueHD
    - Support provided by ExoPlayer FFmpeg extension
- **Subtitles**: SRT, SSA, ASS, TTML, VTT, DVB
    - SSA/ASS styling supported with embedded styles option

## Features

- Native Android app with fluid and intuitive interface
- Completely free, open source, and ad-free
- Hardware and software decoders (MediaCodec & bundled FFmpeg)
- Audio/Subtitle track selection and embedded styles
- Vertical swipe to change brightness (left) / volume (right)
- Horizontal swipe to seek through video
- Material 3 (You) dynamic color theming
- Media picker with tree, folder, and file view modes
- Direct network stream and URL playback
- Storage Access Framework (SAF) document picker integration
- Picture-in-picture mode & background playback
- Android TV support with DPAD navigation
- Private encrypted/PIN-protected Vault
- Network storage support (SMB/FTP/SFTP/WebDAV)
- Local and M3U URL/file playlist support

## Contributing

Contributions are welcome!

### Developer documentation

- [Runtime decoder switching](feature/player/DECODER_SWITCHING.md)

### Translating

You can help translate Kinetix on [Hosted Weblate](https://hosted.weblate.org/engage/next-player/).

[![Translate status](https://hosted.weblate.org/widgets/next-player/-/multi-auto.svg)](https://hosted.weblate.org/engage/next-player/)

## Credits

### Open Source Projects

- [Findroid](https://github.com/jarnedemeulemeester/findroid)
- [Just (Video) Player](https://github.com/moneytoo/Player)
- [LibreTube](https://github.com/libre-tube/LibreTube)
- [ReadYou](https://github.com/Ashinch/ReadYou)
- [Seal](https://github.com/JunkFood02/Seal)

### Special Thanks

[<img src="https://hosted.weblate.org/widgets/next-player/-/287x66-white.png" width="200"/>](https://hosted.weblate.org/engage/next-player/)

Thanks to **Weblate** for providing free hosting for the project.

## License

Kinetix is licensed under the GNU General Public License v3.0. See the [LICENSE](LICENSE) file for more information.
