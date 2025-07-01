package com.mleon.core.model

import com.mleon.core.model.dtos.CartItemDto

data class OrderItem(
    val product: Product,
    var quantity: Int = 1,
    val price: Double = product.price
)

//En realidad es un Order Item DTO, pero la API lo tiene con ese nombre. TODO: Unificar.
fun OrderItem.toDto(): CartItemDto {
    return CartItemDto(
        name = product.name,
        description = product.description,
        imageUrl = product.imageUrl ?: "",
        price = product.price,
        includesDrink = product.includesDrink,
        categories = product.category.map { it.name },
        quantity = quantity
    )
}