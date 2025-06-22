package com.mleon.core.data.model

data class RemoteProduct(
    val name: String,
    val _id: String,
    val description: String,
    val imageUrl: String,
    val price: Double,
    val includesDrink: Boolean,
    val categories: List<String>?,
    val createdAt: String,
    val updatedAt: String,
)