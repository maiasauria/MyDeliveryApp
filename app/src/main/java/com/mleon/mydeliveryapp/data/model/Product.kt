package com.mleon.mydeliveryapp.data.model

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val includesDrink: Boolean,
    val imageUrl: String? = null,
    val category: String? = null
)