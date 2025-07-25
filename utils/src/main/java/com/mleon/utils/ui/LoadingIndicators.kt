package com.mleon.utils.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun YappLoadingDots(modifier: Modifier = Modifier) {
    val dotsCount = 3
    val maxAlpha = 1f
    val minAlpha = 0f
    val ballDiameter = 40f
    val horizontalSpace = 20f
    val animationDuration = 600
    val color = MaterialTheme.colorScheme.primary

    val scales: List<Float> = (0 until dotsCount).map { index ->
        var scale by remember { mutableStateOf(maxAlpha) }
        LaunchedEffect(Unit) {
            delay(animationDuration / dotsCount * index.toLong())
            animate(
                initialValue = minAlpha,
                targetValue = maxAlpha,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = animationDuration,
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Reverse,
                ),
            ) { value, _ ->
                scale = value
            }
        }
        scale
    }

    Canvas(modifier = modifier) {
        val xOffset = ballDiameter + horizontalSpace
        for (index in 0 until dotsCount) {
            drawCircle(
                color = color,
                radius = (ballDiameter / 2) * scales[index],
                center = Offset(
                    x = when {
                        index < dotsCount / 2 -> -(center.x + xOffset)
                        index == dotsCount / 2 -> center.x
                        else -> center.x + xOffset
                    },
                    y = 0f
                )
            )
        }
    }
}

@Composable
fun YappFullScreenLoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        YappLoadingDots()
    }
}
@Composable
fun YappLogoLoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LogoImage()
            Spacer(modifier = Modifier.height(24.dp))
            YappLoadingDots()
        }
    }
}
@Composable
fun YappSmallLoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        contentAlignment = Alignment.Center
    ) {
        YappLoadingDots()
    }
}

@Preview(showBackground = true)
@Composable
fun YappLoadingIndicatorPreview() {
    YappFullScreenLoadingIndicator()
}

@Preview(showBackground = true)
@Composable
fun YappFullScreenLoadingIndicatorPreview() {
    YappLogoLoadingIndicator()
}

@Preview(showBackground = true)
@Composable
fun YappSmallLoadingIndicatorPreview() {
    YappSmallLoadingIndicator()
}