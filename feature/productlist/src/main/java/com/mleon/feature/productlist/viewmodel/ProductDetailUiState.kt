package com.mleon.feature.productlist.viewmodel

import com.mleon.core.model.Product

sealed class ProductDetailUiState {
    object Loading : ProductDetailUiState()
    data class Success(
        val product: Product,
        val quantityInCart: Int,
        val message: String = ""
    ) : ProductDetailUiState()
    data class Error(val error: Exception) : ProductDetailUiState()
}
