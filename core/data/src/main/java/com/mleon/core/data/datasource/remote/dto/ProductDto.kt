package com.mleon.core.data.datasource.remote.dto

import com.mleon.core.model.Product
import com.mleon.core.model.enums.Categories

data class ProductDto(
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

fun ProductDto.toDomain(): Product {
    val safeCategory = categories?.mapNotNull { runCatching { Categories.valueOf(it) }.getOrNull() } ?: emptyList()
    return Product(
        id = _id,
        name = name,
        description = description,
        price = price,
        imageUrl = imageUrl,
        category = safeCategory,
        includesDrink = includesDrink,
    )
}

