package com.mleon.core.model.result

import com.mleon.core.model.Product

sealed class ProductResult {
    data class Success(val products: List<Product>) : ProductResult()
    data class Error(val message: String) : ProductResult()
}