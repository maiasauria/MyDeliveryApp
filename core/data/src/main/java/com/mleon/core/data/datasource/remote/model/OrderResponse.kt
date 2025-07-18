package com.mleon.core.data.datasource.remote.model

import com.mleon.core.data.datasource.remote.dto.CartItemDto
import com.mleon.core.data.datasource.remote.dto.toCartItem
import com.mleon.core.model.Order

data class OrderResponse(
    val orderId: String,
    val orderItems: List<CartItemDto>,
    val total: Double,
    val timestamp: Long
)

fun OrderResponse.toOrder() =
    Order(
        orderId = orderId,
        orderItems = orderItems.map { it.toCartItem() },
        shippingAddress = "",
        paymentMethod = "",
        total = total,
        timestamp = timestamp
    )
