package com.mleon.feature.cart.viewmodel

import com.mleon.core.model.CartItem

sealed class CartUiState {
    object Loading : CartUiState()
    data class Success(
        val cartItems: List<CartItem>,
        val total: Double,
        val cartMessage: String = "",
        val isProcessing: Boolean = false
    ) : CartUiState()
    data class Error(val message: String) : CartUiState()
}
