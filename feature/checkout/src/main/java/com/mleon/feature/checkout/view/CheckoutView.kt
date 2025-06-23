// feature/checkout/src/main/java/com/mleon/feature/checkout/view/CheckoutView.kt
package com.mleon.feature.checkout.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mleon.core.model.CartItem
import com.mleon.core.model.Product
import com.mleon.utils.R
import com.mleon.utils.toCurrencyFormat
import com.mleon.utils.ui.ListDivider
import com.mleon.utils.ui.ScreenTitle
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutView(
    cartItems: List<CartItem>,
    shippingAddress: String,
    paymentMethod: String,
    onConfirmOrder: () -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    subtotalAmount: Double = 0.0,
    shippingCost: Double = 0.0,
    totalAmount: Double = 0.0,
    onNavigateToOrders: () -> Unit
) {
    var selectedPaymentMethod by remember { mutableStateOf("Cash") }
    val tooltipState = rememberTooltipState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {

            ScreenTitle("Confirmación del pedido")

            // Display the list of products
//            LazyColumn(
//                modifier = Modifier.fillMaxWidth(),
//                verticalArrangement = Arrangement.spacedBy(4.dp),
//            ) {

                if (isLoading) {
//                    item {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
//                    }
                }
//
//                items(cartItems) { item ->
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier
//                            .padding(8.dp)
//                            .fillMaxWidth(),
//                    ) {
//                        AsyncImage(
//                            model = ImageRequest.Builder(LocalContext.current)
//                                .data(item.product.imageUrl)
//                                .crossfade(true)
//                                .build(),
//                            contentDescription = item.product.name,
//                            modifier = Modifier
//                                .height(60.dp)
//                                .width(60.dp)
//                                .clip(RoundedCornerShape(10.dp)),
//                            contentScale = ContentScale.Crop,
//                            error = painterResource(R.drawable.ic_launcher_background),
//                            placeholder = painterResource(R.drawable.ic_launcher_background)
//                        )
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Column {
//                            Text(text = "${item.product.name} x ${item.quantity}")
//                            Text(text = "Subtotal: \$${(item.product.price * item.quantity.toDouble()).toCurrencyFormat()}")
//                        }
//                    }
//                }
//            }

            //Mostrar la cantidad total de productos (cart * cantidad)
            Text(
                text = "Total: ${cartItems.sumOf { it.quantity }} productos.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))
            ListDivider()
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Subtotal: $${(subtotalAmount.toCurrencyFormat())}")
            Text(text = "Costo de envio: $${(shippingCost.toCurrencyFormat())}")
            Text(text = "Total: $${(totalAmount.toCurrencyFormat())}")
            Text(text = "Direccion de envio: $shipping/Address")

            Spacer(modifier = Modifier.height(12.dp))
            ListDivider()

            // Payment Method Selection
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RadioButton(
                        selected = selectedPaymentMethod == "Tarjeta de Credito",
                        onClick = { /* Do nothing, it's disabled */ },
                        enabled = false
                    )

                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = {
                            PlainTooltip(
                            ) { Text("Estamos trabajando en la integración de pagos con tarjeta de crédito. Por favor, elegi otro método de pago por ahora.") }
                        },
                        state = tooltipState,

                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Tarjeta de Credito")
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        tooltipState.show()
                                    }
                                },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(imageVector = Icons.Filled.Info, contentDescription = "Info")
                            }
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RadioButton(
                        selected = selectedPaymentMethod == "Efectivo",
                        onClick = { selectedPaymentMethod = "Efectivo" }
                    )
                    Text("Efectivo")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (errorMessage != null) {
                Text(
                    text = "Error: $errorMessage",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }

        Button(
            onClick = onConfirmOrder,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Text("Confirmar el pedido")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CheckoutViewPreview() {
    val cartItems = listOf(
        CartItem(Product("1", "Product 1", "Product Description", 20.0, false, "image1.jpg"), 2),
        CartItem(Product("2", "Product 2", "Product Description", 30.0, false, "image2.jpg"), 1),
    )
    CheckoutView(
        cartItems = cartItems,
        shippingAddress = "Calle 123",
        paymentMethod = "Efectivo",
        onConfirmOrder = {},
        isLoading = false,
        errorMessage = null,
        subtotalAmount = 100.0,
        shippingCost = 20.0,
        totalAmount = 120.0,
        onNavigateToOrders = {},
    )
}