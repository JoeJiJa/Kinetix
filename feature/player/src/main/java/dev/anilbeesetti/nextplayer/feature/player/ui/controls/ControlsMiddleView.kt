package dev.anilbeesetti.nextplayer.feature.player.ui.controls

import androidx.annotation.OptIn
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import dev.anilbeesetti.nextplayer.core.ui.components.GlassSurface
import dev.anilbeesetti.nextplayer.core.ui.theme.NextPlayerTheme
import dev.anilbeesetti.nextplayer.feature.player.buttons.NextButton
import dev.anilbeesetti.nextplayer.feature.player.buttons.PlayPauseButton
import dev.anilbeesetti.nextplayer.feature.player.buttons.PreviousButton
import dev.anilbeesetti.nextplayer.feature.player.ui.preview.rememberPreviewPlayer

@OptIn(UnstableApi::class)
@Composable
fun ControlsMiddleView(modifier: Modifier = Modifier, player: Player?) {
    val playPauseFocusRequester = remember { FocusRequester() }
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        GlassSurface(
            shape = CircleShape,
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 8.dp)
                    .focusProperties { onEnter = { playPauseFocusRequester.requestFocus() } }
                    .focusGroup(),
                horizontalArrangement = Arrangement.spacedBy(
                    32.dp,
                    alignment = Alignment.CenterHorizontally,
                ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PreviousButton(player = player)
                PlayPauseButton(
                    player = player,
                    modifier = Modifier.focusRequester(focusRequester = playPauseFocusRequester),
                )
                NextButton(player = player)
            }
        }
    }
}

@OptIn(UnstableApi::class)
@Preview
@Composable
private fun ControlsMiddleViewPreview() {
    NextPlayerTheme(darkTheme = true) {
        Surface {
            ControlsMiddleView(
                player = rememberPreviewPlayer(),
            )
        }
    }
}
