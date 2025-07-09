package com.mleon.core.data.model

import com.mleon.core.model.Order

sealed class OrderResult {
    data class Success(val order: Order) : OrderResult()
    data class SuccessList(val orders: List<Order>) : OrderResult()
    data class Error(val message: String, val code: Int? = null) : OrderResult()
}