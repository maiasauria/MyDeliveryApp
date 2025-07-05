package com.mleon.utils.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mleon.utils.R
import kotlinx.coroutines.delay

@Composable
fun LogoImage() {
    Image(
        painter = painterResource(id = R.drawable.main_logo),
        contentDescription = "Logo",
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
    )
}

@Composable
fun ListDivider(
    modifier: Modifier = Modifier.padding(horizontal = 8.dp),
    thickness: Dp = 1.dp,
) {
    HorizontalDivider(
        modifier = modifier,
        thickness = thickness,
    )
}

@Composable
fun FullScreenLoadingIndicator() {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}


@Composable
fun ScreenTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineMedium,
    )
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun ScreenSubTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
    )
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun ImageLoader(
    url: String,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model =
            ImageRequest.Builder(LocalContext.current).data(url).crossfade(true).build(),
        contentDescription = contentDescription,
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .padding(end = 8.dp),
        contentScale = ContentScale.Crop,
        error = painterResource(R.drawable.ic_launcher_background),
        placeholder = painterResource(R.drawable.ic_launcher_background),
    )
}

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
fun YappLoadingIndicator() {
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
fun YappSmallLoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        YappLoadingDots()
    }
}