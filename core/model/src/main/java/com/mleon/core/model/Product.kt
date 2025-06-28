package com.mleon.core.model

import com.mleon.core.model.enums.Categories

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val includesDrink: Boolean,
    val imageUrl: String? = "",
    val category: List<Categories> = emptyList(),
    )