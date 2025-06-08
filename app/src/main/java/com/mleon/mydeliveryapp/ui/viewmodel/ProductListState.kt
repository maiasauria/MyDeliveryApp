package com.mleon.mydeliveryapp.ui.viewmodel

import com.mleon.core.model.Categories
import com.mleon.core.model.Product

data class ProductListState (
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val error: Exception? = null,
    val searchQuery: String = "",
    var selectedCategory: Categories? = null,
    val cartMessage: String = "",
)