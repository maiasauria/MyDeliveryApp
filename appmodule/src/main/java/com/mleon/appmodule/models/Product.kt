package com.mleon.appmodule.models

data class Product(
    val productId: String,
    val name: String,
    val category: String,
    val price: Double = 0.00
)