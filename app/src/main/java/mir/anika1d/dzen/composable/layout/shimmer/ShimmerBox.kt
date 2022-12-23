package mir.anika1d.dzen.composable.layout.shimmer

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun ShimmerBox(
    modifier: Modifier,
    enabled: Boolean = true,
    listColor: List<Color> = listOf(
        MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary
    )
) {
    val transition = rememberInfiniteTransition()
    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = FastOutLinearInEasing
            )
        )
    )
    val brush = Brush.linearGradient(
        colors = listColor,
        start = Offset(0f, 0f),
        end = Offset(
            x = translateAnimation.value,
            y = translateAnimation.value
        )
    )
    if (enabled)
        Box(modifier = modifier.background(brush = brush))
}
