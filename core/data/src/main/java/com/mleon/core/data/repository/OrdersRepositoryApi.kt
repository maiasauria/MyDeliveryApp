package com.mleon.core.data.repository

import com.mleon.core.data.model.OrderRequest
import com.mleon.core.data.model.OrderResponse
import com.mleon.core.data.remote.OrderApiService
import com.mleon.core.model.Order
import javax.inject.Inject

class OrdersRepositoryApi
    @Inject
    constructor(
        // Inyectamos el ApiService
        private val apiService: OrderApiService,
    ) : OrdersRepository {
        override suspend fun getOrders(): List<Order> =
            apiService
                .getOrders()
                .map { remoteOrder ->
                    Order(
                        orderId = remoteOrder.orderId,
                        status = remoteOrder.status,
                        totalAmount = remoteOrder.totalAmount,
                        date = remoteOrder.date,
                    )
                }

        override suspend fun createOrder(request: OrderRequest): OrderResponse = apiService.createOrder(request)
    }
