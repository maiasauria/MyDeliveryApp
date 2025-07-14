package com.mleon.feature.checkout.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mleon.core.model.CartItem
import com.mleon.core.model.Product
import com.mleon.core.model.enums.PaymentMethod
import com.mleon.feature.checkout.R
import com.mleon.utils.toCurrencyFormat
import com.mleon.utils.ui.ListDivider
import com.mleon.utils.ui.ScreenSubTitle
import com.mleon.utils.ui.ScreenTitle
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutView(
    cartItems: List<CartItem>,
    shippingAddress: String,
    paymentMethod: PaymentMethod,
    onConfirmOrder: () -> Unit,
    onPaymentMethodSelection: (PaymentMethod) -> Unit = {},
    subtotalAmount: Double = 0.0,
    shippingCost: Double = 0.0,
    totalAmount: Double = 0.0,
    isOrderValid: Boolean = false,
) {
    var selectedPaymentMethod by remember { mutableStateOf<String?>(null) }

    val tooltipState = rememberTooltipState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            ScreenTitle(stringResource(id = R.string.checkout_title))

            // Mostrar la cantidad total de productos (cart * cantidad)
            ScreenSubTitle(stringResource(id = R.string.checkout_total_items))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(cartItems) { cartItem ->
                    PriceRow(
                        name = cartItem.product.name,
                        quantity = cartItem.quantity,
                        price = cartItem.product.price
                    )
                }
                item {
                    PriceRow(
                        name = stringResource(id = R.string.prod_totals),
                        quantity = 0,
                        price = subtotalAmount
                    )
                }


            }
            ListDivider()

            ScreenSubTitle(stringResource(id = R.string.checkout_shipping_address))
            Text(text = shippingAddress)

            ListDivider()

            ScreenSubTitle(stringResource(id = R.string.checkout_payment_method_title))

            // Payment Method Selection
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = paymentMethod == PaymentMethod.CREDIT_CARD,
                        onClick = { /* Do nothing, it's disabled */ },
                        enabled = false,
                    )

                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = {
                            PlainTooltip { Text(stringResource(id = R.string.checkout_credit_card_tooltip)) }
                        },
                        state = tooltipState,
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(PaymentMethod.CREDIT_CARD.displayName)
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        tooltipState.show()
                                    }
                                },
                                //   modifier = Modifier.size(20.dp),
                            ) {
                                Icon(imageVector = Icons.Filled.Info, contentDescription = "Info")
                            }
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = paymentMethod == PaymentMethod.CASH,
                        onClick = {
                            onPaymentMethodSelection(PaymentMethod.CASH)
                        }
                    )
                    Text(PaymentMethod.CASH.displayName)
                }
            }

            ListDivider()
            Spacer(modifier = Modifier.height(12.dp))

            SummaryRow(
                label = stringResource(id = R.string.checkout_subtotal),
                value = subtotalAmount.toCurrencyFormat()
            )
            SummaryRow(
                label = stringResource(id = R.string.checkout_shipping_cost),
                value = shippingCost.toCurrencyFormat()
            )
            SummaryRow(
                label = stringResource(id = R.string.checkout_total),
                value = totalAmount.toCurrencyFormat(),
                isBold = true
            )
        }

        Button(
            onClick = onConfirmOrder,
            enabled = isOrderValid,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(id = R.string.checkout_confirm_order))
        }
    }
}

@Composable
fun PaymentMethodSelection() {
    // TODO implement
}

@Preview(showBackground = true)
@Composable
private fun CheckoutViewPreview() {
    val cartItems =
        listOf(
            CartItem(
                Product("1", "Product 1", "Product Description", 20.0, false, "image1.jpg"),
                2
            ),
            CartItem(
                Product("2", "Product 2", "Product Description", 30.0, false, "image2.jpg"),
                1
            ),
        )
    CheckoutView(
        cartItems = cartItems,
        shippingAddress = "Calle 123",
        paymentMethod = PaymentMethod.CASH,
        onConfirmOrder = {},
        subtotalAmount = 100.0,
        shippingCost = 20.0,
        totalAmount = 120.0,
    )
}

@Composable
fun PriceRow(
    name: String,
    quantity: Int,
    price: Double,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = if (quantity > 0) "$name x $quantity" else name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = price.toCurrencyFormat(),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
fun SummaryRow(label: String, value: String, isBold: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = if (isBold) MaterialTheme.typography.titleLarge
            else MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = if (isBold) MaterialTheme.typography.titleLarge
            else MaterialTheme.typography.bodyMedium
        )
    }
}