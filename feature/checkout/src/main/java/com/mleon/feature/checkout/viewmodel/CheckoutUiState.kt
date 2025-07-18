package com.mleon.feature.checkout.viewmodel

import com.mleon.core.model.CartItem
import com.mleon.core.model.enums.PaymentMethod

sealed class CheckoutUiState {
    data object Loading : CheckoutUiState()
    data class Success(
        val cartItems: List<CartItem>,
        val paymentMethod: PaymentMethod,
        val shippingAddress: String,
        val shippingCost: Double,
        val totalAmount: Double,
        val subTotalAmount: Double,
        val orderConfirmed: Boolean = false,
        val validOrder: Boolean = false
    ) : CheckoutUiState()
    data class Error(val errorMessage: String) : CheckoutUiState()
    data object MissingAddress : CheckoutUiState()
}
