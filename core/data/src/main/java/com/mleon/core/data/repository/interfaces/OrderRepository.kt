package com.mleon.core.data.repository.interfaces

import com.mleon.core.data.model.OrderRequest
import com.mleon.core.data.model.OrderResponse
import com.mleon.core.model.Order

interface OrderRepository {
    suspend fun getOrders(): List<Order>
    suspend fun createOrder(request: OrderRequest): OrderResponse
}