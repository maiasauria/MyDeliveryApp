package com.mleon.core.data.datasource.remote.model

import com.mleon.core.model.Order

data class OrderDto(
    val orderId: String,
    val productIds: List<CartItemDto>,
    val shippingAddress: String? = null,
    val paymentMethod: String? = null,
    val total: Double,
    val timestamp: Long = System.currentTimeMillis()
)

fun Order.toDto(): OrderDto {
    return OrderDto(
        orderId = orderId,
        productIds = orderItems.map { it.toDto() },
        shippingAddress = shippingAddress,
        paymentMethod = paymentMethod,
        total = total,
        timestamp = timestamp
    )
}

fun OrderDto.toOrder(): Order =
    Order(
        orderId = orderId,
        orderItems = productIds.map { it.toCartItem() },
        shippingAddress = shippingAddress ?: "",
        paymentMethod = paymentMethod ?: "",
        total = total,
        timestamp = timestamp,
    )
