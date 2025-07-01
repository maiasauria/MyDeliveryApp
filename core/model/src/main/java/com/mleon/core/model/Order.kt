package com.mleon.core.model

data class Order(
    val orderId: String,
    val orderItems: List<CartItem>,
    val shippingAddress: String,
    val paymentMethod: String,
    val total: Double,
    val timestamp: Long
)