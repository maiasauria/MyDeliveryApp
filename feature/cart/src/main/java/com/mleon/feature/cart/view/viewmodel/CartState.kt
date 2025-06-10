package com.mleon.feature.cart.view.viewmodel

import com.mleon.core.model.CartItem

data class CartState(
    val cartItems: List<CartItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val totalPrice: Double = 0.0
)