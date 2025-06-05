package com.mleon.mydeliveryapp.view.viewmodel

import com.mleon.core.model.Product

data class ProductListState (
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val error: Exception? = null
)