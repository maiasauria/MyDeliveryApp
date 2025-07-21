package com.mleon.feature.cart.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mleon.feature.cart.R

@Composable
fun EmptyCartView(onContinueShoppingClick: () -> Unit) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.cart_empty_padding)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.cart_empty_cart),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.cart_empty_text_vertical_padding))
            )

            Button(
                onClick = onContinueShoppingClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(id = R.string.cart_btn_action_emptycart))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyCartViewPreview() {
    EmptyCartView(onContinueShoppingClick = { })
}
