package com.mleon.feature.orders.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mleon.core.model.Order
import com.mleon.core.model.enums.PaymentMethod
import com.mleon.feature.orders.R
import com.mleon.utils.ui.ListDivider
import com.mleon.utils.ui.ScreenTitle

@Composable
fun OrdersListView(
    orders: List<Order>,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        ScreenTitle(stringResource(R.string.orders_title))
        Box(modifier = Modifier.weight(1f, fill = false)) {
            LazyColumn {
                itemsIndexed(orders) { index, order ->
                    OrderCard(order = order, position = index + 1)
                    if (index < orders.lastIndex) {
                        Spacer(modifier = Modifier.padding(vertical = 8.dp))
                        ListDivider()
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
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),

        ) {
        Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Row (modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,) {
                Text(
                    text = stringResource(R.string.orders_title_card, position),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    stringResource(R.string.orders_total, order.total)
                )
            }

            Text(
                style = MaterialTheme.typography.bodySmall,
                text = stringResource(
                    R.string.orders_date,
                    java.text.SimpleDateFormat("yyyy-MM-dd HH:mm")
                        .format(java.util.Date(order.timestamp))
                )
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
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview(showBackground = true)
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
            ),Order(
                orderId = "1",
                orderItems = listOf(),
                shippingAddress = "123 Main St",
                total = 100.0,
                timestamp = System.currentTimeMillis(),
                paymentMethod = PaymentMethod.CREDIT_CARD.displayName
            )
        )
    )
}