package com.example.uicompose.base

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.example.uicompose.theme.AppDimens
import com.example.uicompose.theme.white
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.runtime.getValue

@Composable
fun AppLoading(
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "loading")
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 900,
                easing = LinearEasing
            )
        ),
        label = "rotation"
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .size(AppDimens.Dp55)
                .rotate(rotation)
        ) {
            val center = this.center
            val strokeWidth = 2.5.dp.toPx()
            val startRadius = size.minDimension * 0.25f
            val endRadius = size.minDimension * 0.42f

            repeat(12) { index ->
                val angle = Math.toRadians((index * 30).toDouble() - 90)
                val alpha = (1f - index * 0.08f).coerceAtLeast(0.1f)

                val start = Offset(
                    x = center.x + cos(angle).toFloat() * startRadius,
                    y = center.y + sin(angle).toFloat() * startRadius
                )

                val end = Offset(
                    x = center.x + cos(angle).toFloat() * endRadius,
                    y = center.y + sin(angle).toFloat() * endRadius
                )

                drawLine(
                    color = white.copy(alpha = alpha),
                    start = start,
                    end = end,
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}