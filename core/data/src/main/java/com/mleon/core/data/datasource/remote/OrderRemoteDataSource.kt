package com.mleon.core.data.datasource.remote

import com.mleon.core.data.remote.OrderApiService
import com.mleon.core.data.repository.interfaces.OrdersRepository
import com.mleon.core.model.Order
import com.mleon.core.data.model.OrderRequest
import com.mleon.core.data.model.OrderResponse
import javax.inject.Inject

class OrderRemoteDataSource
    @Inject
    constructor(
        // Inyectamos el ApiService
        private val apiService: OrderApiService,
    ) : OrdersRepository {
        override suspend fun getOrders(): List<Order> =
            apiService
                .getOrders()
                .map { remoteOrder -> //TODO crear metodos de mapeo
                    Order(
                        orderId = remoteOrder.orderId,
                        productIds = remoteOrder.productIds,
                        shippingAddress = remoteOrder.shippingAddress,
                        paymentMethod = remoteOrder.paymentMethod,
                        total = remoteOrder.total,
                        timestamp = remoteOrder.timestamp

                    )
                }

        override suspend fun createOrder(request: OrderRequest): OrderResponse = apiService.createOrder(request)
    }
