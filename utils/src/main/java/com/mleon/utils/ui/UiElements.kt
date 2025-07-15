package com.mleon.utils.ui

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mleon.utils.R

@Composable
fun LogoImage() {
    Icon(
        painter = painterResource(id = R.drawable.main_logo),
        contentDescription = "Logo",
        tint = MaterialTheme.colorScheme.primary,
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(id = R.dimen.logo_image_bottom_padding)),
    )
}

@Composable
fun ListDivider() {
    Spacer(modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.list_divider_vertical_padding)))
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.list_divider_horizontal_padding)),
        thickness = dimensionResource(id = R.dimen.list_divider_thickness),
    )
    Spacer(modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.list_divider_vertical_padding)))
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
        style = MaterialTheme.typography.headlineMedium

    )
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.screen_title_bottom_spacing)))
}

@Composable
fun ScreenSubTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
    )
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.screen_subtitle_bottom_spacing)))
}

@Composable
fun ImageLoader(
    url: String = "",
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model =
            ImageRequest.Builder(LocalContext.current).data(url).crossfade(true).build(),
        contentDescription = contentDescription,
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.image_loader_corner_radius))),
        contentScale = ContentScale.Crop,
        error = painterResource(R.drawable.ic_launcher_background),
        placeholder = painterResource(R.drawable.ic_launcher_background),
    )
}
