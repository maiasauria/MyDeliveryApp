package com.mleon.core.data.datasource

import com.mleon.core.data.model.OrderDto
import com.mleon.core.data.model.OrderApiResponse
import com.mleon.core.model.Order

interface OrderDataSource {
    suspend fun getOrders(): List<Order>
    suspend fun createOrder(request: OrderDto): OrderApiResponse
}