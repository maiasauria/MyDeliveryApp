package com.mleon.core.data.model

data class OrderRequest(
    val orderId: String,
    val productIds: List<CartItemDto>,
    val shippingAddress: String,
    val paymentMethod: String,
    val total: Double,
    val timestamp: Long = System.currentTimeMillis()
)

data class CartItemDto(
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Double,
    val includesDrink: Boolean,
    val categories: List<String>,
    val quantity: Int
)

data class OrderResponse(
    val orderId: String,
    val productIds: List<CartItemDto>,
    val total: Double,
    val timestamp: Long
)