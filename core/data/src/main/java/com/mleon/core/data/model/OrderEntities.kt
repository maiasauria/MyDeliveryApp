package com.mleon.core.data.model

import com.mleon.core.data.datasource.local.entities.OrderEntity
import com.mleon.core.model.Order
import com.mleon.core.model.dtos.CartItemDto
import com.mleon.core.model.dtos.toDto


data class OrderDto(
    val orderId: String,
    val productIds: List<CartItemDto>,
    val shippingAddress: String,
    val paymentMethod: String,
    val total: Double,
    val timestamp: Long = System.currentTimeMillis()
)
//TODO unificar en Model.

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

fun Order.toEntity(): OrderEntity {
    return OrderEntity(
        id = orderId,
        shippingAddress = shippingAddress,
        paymentMethod = paymentMethod,
        totalAmount = total,
        orderDate = timestamp
    )
}

data class OrderResponse(
    val orderId: String,
    val productIds: List<CartItemDto>,
    val total: Double,
    val timestamp: Long
)