package com.mleon.feature.productlist.viewmodel

import com.mleon.core.model.Product
import com.mleon.core.model.enums.Categories

data class ProductListState (
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val error: Exception? = null,
    val searchQuery: String = "",
    var selectedCategory: Categories? = null,
    val cartMessage: String = "",
)