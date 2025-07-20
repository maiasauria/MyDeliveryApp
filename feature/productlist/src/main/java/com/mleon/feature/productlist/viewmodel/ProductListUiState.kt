package com.mleon.feature.productlist.viewmodel

import com.mleon.core.model.Product
import com.mleon.core.model.enums.Categories

sealed class ProductListUiState {
    object Loading : ProductListUiState()
    data class Success(
        val products: List<Product>,
        val searchQuery: String = "",
        val selectedCategory: Categories? = null,
        val cartMessage: String = "",
        val isAddingToCart: Boolean = false,

    ) : ProductListUiState()
    data class Error(val error: String) : ProductListUiState()
}

data class ProductListViewActions(
    val onSearchQueryChange: (String) -> Unit,
    val onCategorySelection: (Categories?) -> Unit,
    val onOrderByPriceDescending: () -> Unit,
    val onOrderByPriceAscending: () -> Unit,
    val onAddToCart: (Product) -> Unit,
    val onProductClick: (String) -> Unit,
    val onOrderByNameAscending: () -> Unit,
    val onOrderByNameDescending: () -> Unit,
    val onRefresh: () -> Unit
)

data class ProductListViewParams(
    val selectedCategory: Categories? = null,
    val searchQuery: String = "",
    val products: List<Product> = emptyList(),
    val isAddingToCart: Boolean  = false
)
