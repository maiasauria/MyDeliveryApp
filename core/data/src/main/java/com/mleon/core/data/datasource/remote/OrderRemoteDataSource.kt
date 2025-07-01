package com.mleon.core.data.datasource.remote

import android.util.Log
import com.mleon.core.data.datasource.OrderDataSource
import com.mleon.core.data.datasource.remote.model.toOrder
import com.mleon.core.data.model.OrderDto
import com.mleon.core.data.model.OrderResponse
import com.mleon.core.data.remote.OrderApiService
import com.mleon.core.model.Order
import javax.inject.Inject


class OrderRemoteDataSource
    @Inject
    constructor(
        // Inyectamos el ApiService
        private val apiService: OrderApiService,
    ) : OrderDataSource {
        override suspend fun getOrders(): List<Order> =
                apiService.getOrders()
                    .map { remoteOrder ->
                        Log.d("OrderRemoteDataSource", "Remote order: $remoteOrder")
                        remoteOrder.toOrder()
                    }

        override suspend fun createOrder(request: OrderDto): OrderResponse = apiService.createOrder(request)
    }
