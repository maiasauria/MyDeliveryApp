package com.mleon.core.data.repository

import com.mleon.core.data.model.OrderRequest
import com.mleon.core.data.model.OrderResponse
import com.mleon.core.model.Order

interface OrdersRepository {
    suspend fun getOrders(): List<Order>
    suspend fun createOrder(request: OrderRequest): OrderResponse

}