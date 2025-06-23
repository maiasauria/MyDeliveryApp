package com.mleon.core.model

data class Order(
    val orderId: String,
    val status: String,
    val totalAmount: Double,
    val date: String
)