package com.mleon.core.data.datasource

import com.mleon.core.data.datasource.remote.model.OrderDto
import com.mleon.core.data.model.OrderResult

interface OrderDataSource {
    suspend fun getOrders(): OrderResult
    suspend fun createOrder(request: OrderDto): OrderResult
}