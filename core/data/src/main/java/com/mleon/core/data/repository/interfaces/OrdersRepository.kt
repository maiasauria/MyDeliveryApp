package com.mleon.core.data.repository.interfaces

import com.mleon.core.model.Order
import com.mleon.core.data.model.OrderRequest
import com.mleon.core.data.model.OrderResponse

interface OrdersRepository  {
    suspend fun getOrders(): List<Order>
    suspend fun createOrder(request: OrderRequest): OrderResponse
}