package com.mleon.orders.viewmodel

import com.mleon.core.model.Order

data class CheckoutState (
    val isLoading: Boolean = false,
    val order: Order,

    val errorMessage: String? = null,
) {

}