package dev.anilbeesetti.nextplayer.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Mochy dark glass translucent surface color.
 */
val MochyGlassBackground = Color(0xFF101216).copy(alpha = 0.60f)

/**
 * Mochy specular border gradient for dark glassmorphic surfaces.
 * A 1.dp linear gradient border creating a subtle top-lit specular edge.
 */
val MochyGlassSpecularBorderBrush = Brush.linearGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.22f),
        Color.White.copy(alpha = 0.08f),
        Color.White.copy(alpha = 0.02f),
        Color.White.copy(alpha = 0.06f),
    ),
    start = Offset(0f, 0f),
    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
)

/**
 * Mochy subtle card border gradient for video/folder cards.
 */
val MochyCardBorderBrush = Brush.linearGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.14f),
        Color.White.copy(alpha = 0.04f),
        Color.White.copy(alpha = 0.01f),
        Color.White.copy(alpha = 0.05f),
    ),
)

/**
 * Mochy glass badge background for video duration and metadata chips.
 */
val MochyBadgeBackground = Color(0xFF101216).copy(alpha = 0.72f)
val MochyBadgeBorderBrush = Brush.linearGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.25f),
        Color.White.copy(alpha = 0.06f),
    ),
)

/**
 * Mochy glass surface wrapper:
 * Dark translucent background (Color(0xFF101216).copy(alpha = 0.60f)),
 * 22.dp rounded corners (or custom shape), and a 1.dp linear gradient specular border.
 */
@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(22.dp),
    backgroundColor: Color = MochyGlassBackground,
    borderBrush: Brush = MochyGlassSpecularBorderBrush,
    borderWidth: Dp = 1.dp,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(color = backgroundColor, shape = shape)
            .border(
                width = borderWidth,
                brush = borderBrush,
                shape = shape,
            ),
        contentAlignment = contentAlignment,
        content = content,
    )
}

/**
 * Mochy bouncy spring press feedback modifier using [animateFloatAsState]
 * with [Spring.DampingRatioLowBouncy] and [Spring.StiffnessMediumLow].
 */
fun Modifier.bouncyClickable(
    enabled: Boolean = true,
    pressedScale: Float = 0.92f,
    onClick: (() -> Unit)? = null,
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) pressedScale else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow,
        ),
        label = "bouncyScale",
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .then(
            if (onClick != null) {
                Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled,
                    onClick = onClick,
                )
            } else {
                Modifier
            },
        )
}

/**
 * Mochy bouncy scale modifier based on an external [MutableInteractionSource].
 */
fun Modifier.bouncyPress(
    interactionSource: MutableInteractionSource,
    enabled: Boolean = true,
    pressedScale: Float = 0.92f,
): Modifier = composed {
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) pressedScale else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow,
        ),
        label = "bouncyPressScale",
    )
    this.graphicsLayer {
        scaleX = scale
        scaleY = scale
    }
}

/**
 * Mochy smooth animated visibility transitions for HUD / capsule overlays.
 */
object MochyMotionDefaults {
    val HudEnter: EnterTransition = fadeIn(animationSpec = tween(220)) +
        scaleIn(
            initialScale = 0.94f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMediumLow,
            ),
        )

    val HudExit: ExitTransition = fadeOut(animationSpec = tween(180)) +
        scaleOut(
            targetScale = 0.96f,
            animationSpec = tween(180),
        )

    val TopBarEnter: EnterTransition = fadeIn(animationSpec = tween(220)) +
        slideInVertically(
            initialOffsetY = { -it / 2 },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMediumLow,
            ),
        )

    val TopBarExit: ExitTransition = fadeOut(animationSpec = tween(180)) +
        slideOutVertically(
            targetOffsetY = { -it / 2 },
            animationSpec = tween(180),
        )

    val BottomBarEnter: EnterTransition = fadeIn(animationSpec = tween(220)) +
        slideInVertically(
            initialOffsetY = { it / 2 },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMediumLow,
            ),
        )

    val BottomBarExit: ExitTransition = fadeOut(animationSpec = tween(180)) +
        slideOutVertically(
            targetOffsetY = { it / 2 },
            animationSpec = tween(180),
        )
}

/**
 * Sleek fluid Mochy timeline seekbar track canvas.
 */
@Composable
fun MochyFluidSeekBarTrack(
    fraction: Float,
    modifier: Modifier = Modifier,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = Color.White.copy(alpha = 0.18f),
    activeHeight: Dp = 6.dp,
    inactiveHeight: Dp = 4.dp,
    isActive: Boolean = false,
) {
    val currentFraction = fraction.coerceIn(0f, 1f)
    val animatedFraction by animateFloatAsState(
        targetValue = currentFraction,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "fluidTrackFraction",
    )
    val animatedHeight by animateFloatAsState(
        targetValue = if (isActive) activeHeight.value else inactiveHeight.value,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow,
        ),
        label = "fluidTrackHeight",
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(20.dp),
    ) {
        val strokePx = animatedHeight.dp.toPx()
        val corner = CornerRadius(strokePx / 2f, strokePx / 2f)
        val centerY = size.height / 2f

        // Inactive background bar
        drawRoundRect(
            color = inactiveColor,
            topLeft = Offset(0f, centerY - strokePx / 2f),
            size = Size(size.width, strokePx),
            cornerRadius = corner,
        )

        // Active played portion
        val activeWidth = (size.width * animatedFraction).coerceIn(0f, size.width)
        if (activeWidth > 0f) {
            drawRoundRect(
                color = activeColor,
                topLeft = Offset(0f, centerY - strokePx / 2f),
                size = Size(activeWidth, strokePx),
                cornerRadius = corner,
            )
        }
    }
}
