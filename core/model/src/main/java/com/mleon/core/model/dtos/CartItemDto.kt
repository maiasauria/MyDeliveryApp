package com.mleon.core.model.dtos

import com.mleon.core.model.CartItem

//Esta clase no tiene ID del producto, no se usa en la app.
data class CartItemDto(
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Double,
    val includesDrink: Boolean,
    val categories: List<String>,
    val quantity: Int
)

fun CartItem.toDto(): CartItemDto = CartItemDto(
    name = product.name,
    description = product.description,
    imageUrl = product.imageUrl ?: "",
    price = product.price,
    includesDrink = product.includesDrink,
    categories = product.category.map { it.name },
    quantity = quantity
)