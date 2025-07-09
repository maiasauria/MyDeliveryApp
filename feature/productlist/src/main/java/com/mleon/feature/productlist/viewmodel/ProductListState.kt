package com.mleon.feature.productlist.viewmodel

import com.mleon.core.model.Product
import com.mleon.core.model.enums.Categories

sealed class ProductListUiState {
    object Loading : ProductListUiState()
    data class Success(
        val products: List<Product>,
        val searchQuery: String,
        val selectedCategory: Categories?,
        val cartMessage: String,
        val isAddingToCart: Boolean = false
    ) : ProductListUiState()
    data class Error(val error: String) : ProductListUiState()
}