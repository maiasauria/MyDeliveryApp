package com.mleon.feature.cart.view.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mleon.core.model.CartItem
import com.mleon.core.model.Product
import com.mleon.core.model.enums.Categories
import com.mleon.feature.cart.R
import com.mleon.utils.toCurrencyFormat
import com.mleon.utils.ui.ListDivider
import com.mleon.utils.ui.ScreenTitle

@Composable
fun CartView(
    cartItems: List<CartItem>,
    totalPrice: Double,
    isLoading: Boolean,
    onQuantityChange: (product: Product, quantity: Int) -> Unit,
    onRemoveFromCart: (product: Product) -> Unit,
    onCheckoutClick: () -> Unit
    ) {
    Column(modifier = Modifier.padding(16.dp)) {
        ScreenTitle(stringResource(id = R.string.screen_title))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // Usar espacio disponible
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            //Elementos del carrito
            itemsIndexed(cartItems) { index, cartItem ->
                CartProductCard(
                    product = cartItem.product,
                    quantity = cartItem.quantity,
                    onQuantityChange = { product, quantity -> onQuantityChange(product, quantity) },
                    onRemoveFromCart = { onRemoveFromCart(cartItem.product) },
                    isLoading = isLoading
                )
                if (index < cartItems.lastIndex) { //No mostrar el divisor después del último elemento
                    Spacer(modifier = Modifier.padding(vertical = 4.dp))
                    ListDivider()
                }
            }
            item {
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = stringResource(id = R.string.order_summary),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
            items(cartItems) {
                Text(
                    text = "${it.product.name} x ${it.quantity} = $${(it.product.price * it.quantity.toDouble()).toCurrencyFormat()}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            item {
                Text(
                    text = "${stringResource(id = R.string.prod_totals)} ${totalPrice.toCurrencyFormat()}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        ListDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "${stringResource(id = R.string.totals)}${totalPrice.toCurrencyFormat()}",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = { onCheckoutClick() },
                modifier = Modifier.padding(start = 8.dp),
                enabled = cartItems.isNotEmpty() && !isLoading
            ) {
                Text(stringResource(id = R.string.btn_action_cart))
            }
        }
    }

}



@Composable
@Preview(showBackground = true)
fun CartViewPreview() {
    CartView(
        cartItems = listOf(
            CartItem(
                product = Product(
                    id = "1", name = "Producto 1", price = 10.0, includesDrink = false,
                    description = "",
                    category = listOf(Categories.PIZZA)
                ),
                quantity = 2
            ),
            CartItem(
                product = Product(
                    id = "2",
                    name = "Producto 2",
                    price = 20.0,
                    includesDrink = true,
                    description = "",
                    category = listOf(Categories.PIZZA)
                ),
                quantity = 1
            )
        ),
        totalPrice = 40.0,
        isLoading = false,
        onQuantityChange = { _, _ -> },
        onRemoveFromCart = {},
        onCheckoutClick = {}
    )
}