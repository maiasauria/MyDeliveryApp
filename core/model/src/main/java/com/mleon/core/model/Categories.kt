package com.mleon.core.model

enum class Categories {
    PIZZA ,
    PASTA,
    BURGER,
    SALAD,
    MEXICAN,
    VEGETARIAN,
    VEGAN;

    fun getCategoryName(): String {
        return when (this) {
            PIZZA -> "Pizza"
            PASTA -> "Pasta"
            BURGER -> "Hamburguesas"
            SALAD -> "Ensalada"
            MEXICAN -> "Mexicana"
            VEGETARIAN -> "Vegetariana"
            VEGAN -> "Vegana"
        }
    }
}