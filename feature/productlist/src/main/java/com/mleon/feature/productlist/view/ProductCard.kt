package com.mleon.feature.productlist.view

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mleon.core.model.Product
import com.mleon.core.model.enums.Categories
import com.mleon.feature.productlist.R
import com.mleon.utils.toCurrencyFormat
import com.mleon.utils.ui.ImageLoader

@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    product: Product,
    onAddToCart: (Product) -> Unit = { },
    onClick: (Product) -> Unit = { }
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable { onClick(product) },
        shape = RoundedCornerShape(CornerSize(dimensionResource(id = R.dimen.product_card_corner_radius))),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.product_card_height)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ImageLoader(
                url = product.imageUrl ?: "",
                contentDescription = product.name,
                modifier =
                    Modifier
                        .height(dimensionResource(id = R.dimen.product_card_image_size))
                        .width(dimensionResource(id = R.dimen.product_card_image_size))
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.product_card_content_padding))
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = product.description,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = product.price.toCurrencyFormat(),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = dimensionResource(id = R.dimen.product_card_price_top_padding))
                    )
                }
                IconButton(
                    onClick = { onAddToCart(product) },
                    enabled = !isLoading,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                ) {
                    Box(
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.product_card_add_button_size))
                            .background(
                                MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            ),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductCardPreview() {
    ProductCard(
        product =
            Product(
                id = "1",
                name = "Producto de Ejemplo Con Nombre Largo",
                description = "Descripción del producto de ejemplo con Texto Largo.",
                price = 19.99,
                imageUrl = "https://img.freepik.com/free-photo/front-view-burger-stand_141793-15542.jpg",
                includesDrink = false,
                category = listOf(Categories.VEGAN),
            ),
        isLoading = false,
        onAddToCart = { },
    )
}

@Preview(showBackground = true)
@Composable
private fun ProductCardShortPreview() {
    ProductCard(
        product =
            Product(
                id = "1",
                name = "Producto de Ejemplo Con Nombre Largo",
                description = "Descripción del producto",
                price = 19.99,
                imageUrl = "https://img.freepik.com/free-photo/front-view-burger-stand_141793-15542.jpg",
                includesDrink = false,
                category = listOf(Categories.VEGAN),
            ),
        isLoading = false,
        onAddToCart = { },
    )
}


enum class ButtonState { Pressed, Idle }
fun Modifier.bounceClick() = composed {
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val scale by animateFloatAsState(if (buttonState == ButtonState.Pressed) 0.70f else 1f)

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = {  }
        )
        .pointerInput(buttonState) {
            awaitPointerEventScope {
                buttonState = if (buttonState == ButtonState.Pressed) {
                    waitForUpOrCancellation()
                    ButtonState.Idle
                } else {
                    awaitFirstDown(false)
                    ButtonState.Pressed
                }
            }
        }
}

@Composable
fun PulsateEffect() {
    Button(onClick = {
        // clicked
    }, shape = RoundedCornerShape(dimensionResource(id = R.dimen.product_card_pulsate_corner_radius)),
        contentPadding = PaddingValues(dimensionResource(id = R.dimen.product_card_pulsate_padding)),
        modifier = Modifier.bounceClick()) {
        Text(text = "Click me")
    }
}