package com.mleon.core.data.datasource.remote.dto

import com.mleon.core.model.CartItem
import com.mleon.core.model.OrderItem
import com.mleon.core.model.Product
import com.mleon.core.model.enums.Categories

//Esta clase no tiene ID del producto, no se usa en la app.
data class CartItemDto(
    val productId: String? = null, // Optional, not used in the app
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Double,
    val includesDrink: Boolean,
    val categories: List<String>,
    val quantity: Int
)

fun CartItem.toDto(): CartItemDto {

    // Pre procesamos las categorias para mejorar performance
    val categoryNames = ArrayList<String>(product.category.size)
    for (cat in product.category) {
        categoryNames.add(cat.name)
    }

    return CartItemDto(
        name = product.name,
        description = product.description,
        imageUrl = product.imageUrl ?: "",
        price = product.price,
        includesDrink = product.includesDrink,
        categories = categoryNames,
        quantity = quantity
    )
}


fun CartItemDto.toCartItem(): CartItem {

    val safeCategory = ArrayList<Categories>(categories.size) // Preparamos una lista de categorias seguras para evitar excepciones

    // runCatching para evitar excepciones si la categoria no es valida
    for (cat in categories) {
        runCatching { Categories.valueOf(cat) }.getOrNull()?.let { safeCategory.add(it) }
    }

    return CartItem(
        Product(
            id = productId ?: "",
            name = name,
            description = description,
            imageUrl = imageUrl,
            price = price,
            includesDrink = includesDrink,
            category = safeCategory,
        ),
        quantity = quantity,
    )
}

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