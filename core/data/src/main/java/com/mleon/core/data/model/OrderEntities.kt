package com.mleon.core.data.model

data class OrderRequest(
    val orderId: String,
    val cartItems: List<CartItemDto>,
    val shippingAddress: String,
    val paymentMethod: String,
    val total: Double,
    val timestamp: Long = System.currentTimeMillis()
)

data class CartItemDto(
    val productId: String,
    val quantity: Int
)

data class OrderResponse(
    val orderId: String,
    val status: String
)