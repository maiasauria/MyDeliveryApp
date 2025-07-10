package com.mleon.core.data.datasource.remote.model

import com.mleon.core.model.Product

sealed class ProductResult {
    data class Success(val products: List<Product>) : ProductResult()
    data class Error(val message: String) : ProductResult()
}