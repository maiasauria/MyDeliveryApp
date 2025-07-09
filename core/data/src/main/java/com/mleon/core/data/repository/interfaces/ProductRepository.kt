package com.mleon.core.data.repository.interfaces

import com.mleon.core.data.model.ProductResult
import com.mleon.core.model.Product

interface ProductRepository {
    suspend fun getProducts(refreshData: Boolean): ProductResult
    suspend fun getProductById(productId: String): Product?
}