package com.mleon.core.model

data class Order(
    val orderId: String,
    val productIds: List<CartItem>,
    val shippingAddress: String,
    val paymentMethod: String,
    val total: Double,
    val timestamp: Long
)