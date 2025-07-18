package com.mleon.feature.orders.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
        modifier = Modifier.fillMaxWidth()
    ) {
        ScreenTitle(stringResource(R.string.orders_title))
        Box(modifier = Modifier.weight(1f, fill = false)) {
            LazyColumn {
                itemsIndexed(orders) { index, order ->
                    OrderCard(order = order, position = index + 1)
                    if (index < orders.lastIndex) {
                        ListDivider()
                    }
                }
            }
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
