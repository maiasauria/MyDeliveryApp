package com.mleon.core.data.datasource.remote.dto

import com.mleon.core.model.Order

data class OrderDto(
    val orderId: String,
    val orderItems: List<CartItemDto>,
    val shippingAddress: String? = null,
    val paymentMethod: String? = null,
    val total: Double,
    val timestamp: Long = System.currentTimeMillis()
)

fun Order.toDto(): OrderDto {
    return OrderDto(
        orderId = orderId,
        orderItems = orderItems.map { it.toDto() },
        shippingAddress = shippingAddress,
        paymentMethod = paymentMethod,
        total = total,
        timestamp = timestamp
    )
}

fun OrderDto.toDomain(): Order =
    Order(
        orderId = orderId,
        orderItems = orderItems.map { it.toCartItem() },
        shippingAddress = shippingAddress ?: "",
        paymentMethod = paymentMethod ?: "",
        total = total,
        timestamp = timestamp,
    )
