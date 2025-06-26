package com.mleon.core.data.model

import com.mleon.core.model.CartItemDto

data class OrderRequest(
    val orderId: String,
    val productIds: List<CartItemDto>,
    val shippingAddress: String,
    val paymentMethod: String,
    val total: Double,
    val timestamp: Long = System.currentTimeMillis()
)



data class OrderResponse(
    val orderId: String,
    val productIds: List<CartItemDto>,
    val total: Double,
    val timestamp: Long
)