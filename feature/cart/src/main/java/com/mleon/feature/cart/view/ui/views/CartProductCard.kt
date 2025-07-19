package com.mleon.feature.cart.view.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mleon.core.model.Product
import com.mleon.feature.cart.R
import com.mleon.utils.toCurrencyFormat
import com.mleon.utils.ui.ImageLoader
import com.mleon.feature.cart.R as CartR


@Composable
fun CartProductCard(
    modifier: Modifier = Modifier,
    product: Product,
    isLoading: Boolean,
    quantity: Int,
    onQuantityChange: (product: Product, quantity: Int) -> Unit = { _, _ -> },
    onRemoveFromCart: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(CornerSize(dimensionResource(id = R.dimen.cart_card_corner_radius))),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.cart_card_height)),
        ) {
            CardImage(product)
            Column(
                modifier = Modifier
                    .padding(vertical = dimensionResource(id = R.dimen.cart_card_padding))
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Row(horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.Top)
                {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        CardTitle(productName = product.name)
                        ProductPrice(product.price, quantity)
                    }
                    RemoveFromCartButton(
                        enabled = !isLoading,
                        onRemoveFromCart = onRemoveFromCart,
                        modifier = Modifier.align(Alignment.Top)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    QuantitySelector(
                        product = product,
                        quantity = quantity,
                        onQuantityChange = onQuantityChange,
                        isLoading = isLoading
                    )
                }
            }
        }
    }
}

@Composable
fun QuantitySelector(
    product: Product,
    quantity: Int,
    onQuantityChange: (product: Product, quantity: Int) -> Unit = { _, _ -> },
    isLoading: Boolean = false
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.cart_card_selector_radius))
            )
            .wrapContentHeight()
    ) {
        QuantityButton(
            onClick = {
                if (quantity > 1) {
                    onQuantityChange(product, quantity - 1)
                }
            },
            enabled = quantity > 1 && !isLoading,
            contentDescription = stringResource(id = CartR.string.remove),
            icon = Icons.Filled.Remove
        )
        Text(
            text = quantity.toString(),
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.cart_quantity_text_padding))
        )
        QuantityButton(
            onClick = { onQuantityChange(product, quantity + 1) },
            enabled = !isLoading,
            contentDescription = stringResource(id = CartR.string.add),
            icon = Icons.Default.Add
        )
    }
}

@Composable
fun CardTitle(productName: String = "") {
    Text(
        text = productName,
        style = MaterialTheme.typography.titleMedium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}


@Composable
fun ProductPrice(price: Double = 0.0, productQuantity: Int = 1, modifier: Modifier = Modifier) {
    val priceText = stringResource(
        id = R.string.cart_product_price_format,
        price.toCurrencyFormat(),
        productQuantity,
        (price * productQuantity.toDouble()).toCurrencyFormat()
    )
    Text(
        modifier = modifier,
        text = priceText,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun QuantityButton(
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    contentDescription: String = "",
    icon: ImageVector
) {
    IconButton(
        onClick = onClick,
        enabled = enabled
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}

@Composable
fun RemoveFromCartButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
    onRemoveFromCart: () -> Unit = {}
) {
    IconButton(
        onClick = onRemoveFromCart,
        modifier = modifier
            .padding(start = dimensionResource(id = R.dimen.cart_remove_button_padding))
            .width(dimensionResource(id = R.dimen.cart_remove_button_size))
            .height(dimensionResource(id = R.dimen.cart_remove_button_size)),
        enabled = enabled
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = stringResource(id = CartR.string.remove_from_cart),
            tint = if (enabled) MaterialTheme.colorScheme.primary else Color.Gray,
        )
    }
}

@Composable
fun CardImage(product: Product) {
    ImageLoader(
        url = product.imageUrl ?: "",
        contentDescription = product.name,
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.cart_image_size))
            .width(dimensionResource(id = R.dimen.cart_image_size))
            .padding(end = dimensionResource(id = R.dimen.cart_card_padding))
        ,

    )
}

@Preview(showBackground = false)
@Composable
fun CartProductCardPreview() {
    CartProductCard(
        product = Product(
            id = "1",
            name = "Producto de prueba con un nombre largo",
            description = "Description del producto de prueba con un nombre largo",
            price = 19.99,
            imageUrl = "https://via.placeholder.com/150",
            includesDrink = true,
        ),
        isLoading = false,
        quantity = 2
    )
}

@Preview(showBackground = false)
@Composable
fun CartProductCardShortPreview() {
    CartProductCard(
        product = Product(
            id = "1",
            name = "Producto de prueba",
            description = "Description del producto de prueba",
            price = 19.99,
            imageUrl = "https://via.placeholder.com/150",
            includesDrink = true,
        ),
        isLoading = true,
        quantity = 2
    )
}


