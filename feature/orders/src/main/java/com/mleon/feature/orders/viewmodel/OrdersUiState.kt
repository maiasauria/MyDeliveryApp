package com.mleon.feature.orders.viewmodel

import com.mleon.core.model.Order

data class OrdersUiState(
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)