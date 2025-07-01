package com.mleon.feature.checkout.viewmodel

import com.mleon.core.model.CartItem
import com.mleon.core.model.enums.PaymentMethod

data class CheckoutUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val orderConfirmed: Boolean = false,
    val validOrder: Boolean = false,
    val cartItems: List<CartItem> = emptyList(),
    val paymentMethod: PaymentMethod = PaymentMethod.CASH,
    val shippingAddress: String = "Calle Temporal 120 Depto 123",
    val shippingCost: Double = 10.0,
    val totalAmount: Double = 0.0,
    val subTotalAmount: Double = 0.0,
)