package com.mleon.feature.cart.view.ui.views

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mleon.core.model.CartItem
import com.mleon.core.model.Categories
import com.mleon.core.model.Product
import com.mleon.feature.cart.view.viewmodel.CartState
import com.mleon.utils.toCurrencyFormat
import com.mleon.utils.ui.ListDivider

@Composable
fun CartView(
    state: CartState,
    onQuantityChange: (product: Product, quantity: Int) -> Unit,
    onRemoveFromCart: (product: Product) -> Unit,
    onCheckoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(Color.White)
    ) {
        Text("Mi Carrito", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f), // Take available space, allow content below
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (state.isLoading) {
                item {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }
            items(state.cartItems) { cartItem ->
                CartProductCard(
                    product = cartItem.product,
                    quantity = cartItem.quantity,
                    onQuantityChange = { product, quantity ->
                        onQuantityChange(product, quantity)
                    },
                    onRemoveFromCart = { onRemoveFromCart(cartItem.product) },
                    isLoading = state.isLoading
                )
                ListDivider()
            }
            item {
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = "Resumen del Pedido",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
            items(state.cartItems) {
                Text(
                    text = "${it.product.name} x ${it.quantity} = $${(it.product.price * it.quantity.toDouble()).toCurrencyFormat()}",
                    style = MaterialTheme.typography.bodyMedium
                )

            }
            item {
                Text(
                    text = "Total de Productos: $${state.totalPrice.toCurrencyFormat()}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        ListDivider()
// Pushes the row to the bottom
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Total: $${state.totalPrice.toCurrencyFormat()}",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = { onCheckoutClick() },
                modifier = Modifier.padding(start = 8.dp),
                enabled = state.cartItems.isNotEmpty() && !state.isLoading
            ) {
                Text("Ir a Pagar")
            }
        }

        state.errorMessage?.let { errorMessage ->
            Toast.makeText(
                LocalContext.current,
                "Error: $errorMessage",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}

@Composable
@Preview(showBackground = true)
fun CartViewPreview() {
    CartView(
        state = CartState(
            isLoading = false,
            cartItems = listOf(
                CartItem(
                    product = Product(
                        id = 1, name = "Producto 1", price = 10.0, includesDrink = false,
                        description = "",
                        category = listOf(Categories.PIZZA)
                    ),
                    quantity = 2
                ),
                CartItem(
                    product = Product(
                        id = 2,
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
            errorMessage = null
        ),
        onQuantityChange = { _, _ -> },
        onRemoveFromCart = {},
        onCheckoutClick = {}
    )
}