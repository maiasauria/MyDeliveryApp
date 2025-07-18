package com.mleon.feature.orders.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mleon.core.model.Order
import com.mleon.feature.orders.R
import com.mleon.utils.toCurrencyFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val DATE_DISPLAY_FORMAT = "yyyy-MM-dd HH:mm"

@Composable
fun OrderCard(order: Order, position: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
    ) {
        Column {
            OrderCardHeader(order = order, position = position)
            OrderCardDetails(order = order)
        }
    }
}

@Composable
fun OrderCardHeader(order: Order, position: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.orders_title_card, position),
            style = MaterialTheme.typography.titleLarge

        )
        Text(
            text = order.total.toCurrencyFormat(),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
fun OrderCardDetails(order: Order) {
    Text(
        style = MaterialTheme.typography.bodySmall,
        text = SimpleDateFormat(DATE_DISPLAY_FORMAT, Locale("es", "AR"))
                .format(Date(order.timestamp))
    )
    Text(
        stringResource(
            if(order.orderItems.size == 1) {
                R.string.orders_items_single
            } else {
                R.string.orders_items_plural
            },
            order.orderItems.sumOf { it.quantity }
        ),
        maxLines = 1,
        style = MaterialTheme.typography.bodyMedium,
    )
    Text(order.shippingAddress,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.bodyMedium,
    )
}
@Composable
@Preview(showBackground = true)
fun OrderCardPreview() {
    val order = Order(
        orderId = "1",
        orderItems = listOf(),
        total = 100.0,
        shippingAddress = "Av Corrientes 1234",
        timestamp = System.currentTimeMillis(),
        paymentMethod = "CASH",
    )
    OrderCard(order = order, position = 1)
}
