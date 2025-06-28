package com.mleon.feature.checkout.viewmodel

import com.mleon.core.model.enums.PaymentMethod

data class CheckoutUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val orderConfirmed: Boolean = false,
    val paymentMethod: PaymentMethod = PaymentMethod.CASH,
    val validOrder: Boolean = false
)