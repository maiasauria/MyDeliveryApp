package com.mleon.feature.orders.viewmodel

import com.mleon.core.model.Order

sealed class OrdersUiState {
    object Loading : OrdersUiState()
    data class Success(
        val orders: List<Order>
    ) : OrdersUiState()
    data class Error(val error: Exception) : OrdersUiState()
}

