package com.mleon.core.data.repository.interfaces

import com.mleon.core.model.Product

interface ProductRepository {
    suspend fun getProducts(): List<Product>
    suspend fun filterProducts(name: String): List<Product>
    suspend fun filterProductsByCategory(category: String): List<Product>
}