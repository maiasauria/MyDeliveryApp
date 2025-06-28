package com.mleon.core.data.repository.impl

import com.mleon.core.data.datasource.OrderDataSource
import com.mleon.core.data.model.OrderRequest
import com.mleon.core.data.model.OrderResponse
import com.mleon.core.data.repository.interfaces.OrderRepository
import com.mleon.core.model.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val orderDataSource: OrderDataSource,
) : OrderRepository {
    override suspend fun createOrder(request: OrderRequest): OrderResponse =
        withContext(Dispatchers.IO) {
            orderDataSource.createOrder(request)
        }

    override suspend fun getOrders(): List<Order> =
        withContext(Dispatchers.IO) {
            orderDataSource.getOrders()
        }

}