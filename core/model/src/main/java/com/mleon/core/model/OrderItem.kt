package com.mleon.core.model

data class OrderItem(
    val product: Product,
    var quantity: Int = 1,
    val price: Double = product.price
)

