package com.mleon.feature.orders.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mleon.core.model.Order
import com.mleon.core.model.enums.PaymentMethod
import com.mleon.feature.orders.R
import com.mleon.utils.ui.ListDivider
import com.mleon.utils.ui.ScreenTitle
import com.mleon.utils.ui.YappLoadingIndicator

@Composable
fun OrdersListView(
    orders: List<Order>,
    isLoading: Boolean,
    error: String?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        ScreenTitle(stringResource(R.string.orders_title))
        Box(modifier = Modifier.weight(1f, fill = false)) {
            when {
                isLoading -> {
                    YappLoadingIndicator()
                }
                error != null && error.isNotEmpty() -> {
                    Text(
                        text = stringResource(R.string.orders_error, error),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn {
                        itemsIndexed(orders) { index, order ->
                            OrderCard(order = order, position = index + 1)
                            if (index < orders.lastIndex) {
                                ListDivider(modifier = Modifier.padding(vertical = 4.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCard(order: Order, position: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),

    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.orders_title_card, position),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                stringResource(
                    R.string.orders_quantity,
                    order.orderItems.sumOf { it.quantity }
                ),
            )
            Text(
                stringResource(
                    R.string.orders_shipping_address,
                    order.shippingAddress
                ),
            )
            Text(
                stringResource(
                    R.string.orders_total,
                    order.total
                )
            )
            Text(
                stringResource(
                    R.string.orders_date,
                    java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(java.util.Date(order.timestamp))
                )
            )
        }
    }
}


@Composable
fun OrdersListViewPreview() {
    OrdersListView(
        orders = listOf(
            Order(
                orderId = "1",
                orderItems = listOf(),
                shippingAddress = "123 Main St",
                total = 100.0,
                timestamp = System.currentTimeMillis(),
                paymentMethod = PaymentMethod.CREDIT_CARD.displayName
            )
        ),
        isLoading = false,
        error = null
    )
}