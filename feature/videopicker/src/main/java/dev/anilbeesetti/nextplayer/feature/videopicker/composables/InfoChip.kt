package dev.anilbeesetti.nextplayer.feature.videopicker.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.anilbeesetti.nextplayer.core.ui.components.MochyBadgeBackground
import dev.anilbeesetti.nextplayer.core.ui.components.MochyBadgeBorderBrush

@Composable
fun InfoChip(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MochyBadgeBackground,
    borderBrush: Brush? = MochyBadgeBorderBrush,
    contentColor: Color = Color.White,
    shape: Shape = RoundedCornerShape(6.dp),
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
        color = contentColor,
        modifier = modifier
            .clip(shape = shape)
            .background(color = backgroundColor)
            .then(
                if (borderBrush != null) {
                    Modifier.border(
                        width = 1.dp,
                        brush = borderBrush,
                        shape = shape,
                    )
                } else {
                    Modifier
                },
            )
            .padding(
                vertical = 2.dp,
                horizontal = 6.dp,
            ),
    )
}
