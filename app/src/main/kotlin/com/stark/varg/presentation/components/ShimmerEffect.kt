package com.stark.varg.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/**
 * Reusable high-tech shimmer modifier tailored for the Stark VARG stealth cockpit design.
 * Creates an animated titanium/obsidian light sweep across skeleton elements.
 */
fun Modifier.vargShimmer(
    shape: Shape = RoundedCornerShape(8.dp),
    baseColor: Color = Color(0xFF141418),
    highlightColor: Color = Color(0xFF272733),
): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "VargShimmerTransition")
    val translateAnim by transition.animateFloat(
        initialValue = -600f,
        targetValue = 1400f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1350, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "VargShimmerTranslate",
    )

    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            baseColor,
            highlightColor,
            baseColor,
        ),
        start = Offset(translateAnim, translateAnim),
        end = Offset(translateAnim + 500f, translateAnim + 500f),
    )

    this
        .clip(shape)
        .background(brush = shimmerBrush, shape = shape)
}
