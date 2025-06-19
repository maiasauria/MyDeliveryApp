package com.mleon.core.model

data class Order(
    val orderId: String,
    val userId: String,
    val products: List<CartItem>,
    val totalAmount: Double,
    val shippingAddress: String,
    val shippingPrice: Double,
    val orderDate: String,
    val status: String
)
