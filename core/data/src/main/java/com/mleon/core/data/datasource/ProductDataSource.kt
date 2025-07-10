package com.mleon.core.data.datasource

import com.mleon.core.data.datasource.remote.model.ProductResult

interface ProductDataSource {
    suspend fun getProducts(): ProductResult
}