package com.mleon.core.data.datasource

import com.mleon.core.data.datasource.remote.dto.OrderDto
import com.mleon.core.model.result.OrderResult

interface OrderDataSource {
    suspend fun getOrders(): OrderResult
    suspend fun createOrder(request: OrderDto): OrderResult
}