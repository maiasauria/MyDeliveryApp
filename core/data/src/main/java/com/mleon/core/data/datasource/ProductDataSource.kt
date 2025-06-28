package com.mleon.core.data.datasource

import com.mleon.core.model.Product

interface ProductDataSource {
    suspend fun getProducts(): List<Product>
}