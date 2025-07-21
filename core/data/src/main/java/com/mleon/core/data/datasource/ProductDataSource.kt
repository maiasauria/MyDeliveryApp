package com.mleon.core.data.datasource

import com.mleon.core.model.result.ProductResult

interface ProductDataSource {
    suspend fun getProducts(): ProductResult
}