package com.mleon.orders

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
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.unit.dp
import com.mleon.core.model.Product
import com.mleon.orders.viewmodel.CheckoutState
import com.mleon.utils.toCurrencyFormat
import com.mleon.utils.ui.ListDivider

@Composable
fun CheckoutView(
    state: CheckoutState,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(Color.White)
    ) {
        Text("Mi Pedido", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // Take available space, allow content below
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (state.isLoading) {
                item {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }

            //Elementos del carrito
            itemsIndexed(state.order.products) { index, cartItem ->
                CheckoutProductCard(
                    product = cartItem.product,
                    quantity = cartItem.quantity,
                )
                if (index < state.order.products.lastIndex) { //No mostrar el divisor después del último elemento
                    Spacer(modifier = Modifier.padding(vertical = 4.dp))
                    ListDivider()
                }
            }
            if (state.order.products.isEmpty()) {
                item {
                    Text(
                        text = "Tu carrito está vacío",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            } else {
                item {
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = "Resumen del Pedido",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                items(state.order.products) {
                    Text(
                        text = "${it.product.name} x ${it.quantity} = $${(it.product.price * it.quantity.toDouble()).toCurrencyFormat()}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                }
                item {
                    Text(
                        text = "Total de Productos: $${state.order.totalAmount.toCurrencyFormat()}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
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
                text = "Total de Productos: $${state.order.totalAmount.toCurrencyFormat()}",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {  },
                modifier = Modifier.padding(start = 8.dp),
                enabled = state.order.products.isNotEmpty() && !state.isLoading
            ) {
                Text("Confirmar el pedido")
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
fun CheckoutProductCard(
    product: Product,
    quantity: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "${product.name} x $quantity",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
