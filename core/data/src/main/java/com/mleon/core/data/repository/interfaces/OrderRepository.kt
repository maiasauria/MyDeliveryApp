package com.mleon.core.data.repository.interfaces

import com.mleon.core.data.datasource.remote.model.OrderResult
import com.mleon.core.model.Order

interface OrderRepository {
    suspend fun getOrders(): OrderResult
    suspend fun createOrder(order: Order): OrderResult
}