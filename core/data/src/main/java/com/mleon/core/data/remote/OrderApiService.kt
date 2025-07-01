package com.mleon.core.data.remote

import com.mleon.core.data.datasource.remote.model.RemoteOrder
import com.mleon.core.data.model.OrderDto
import com.mleon.core.data.model.OrderResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface OrderApiService {
    @POST("orders")
    suspend fun createOrder(@Body request: OrderDto): OrderResponse

    @GET("orders")
    suspend fun getOrders(): List<RemoteOrder>
}