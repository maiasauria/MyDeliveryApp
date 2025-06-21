package com.mleon.core.model

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val includesDrink: Boolean,
    val imageUrl: String? = "",
    val category: List<Categories> = emptyList() ,

) {
    fun formatPrice(): String {
        return "$${"%.2f".format(price)}"
    }
}