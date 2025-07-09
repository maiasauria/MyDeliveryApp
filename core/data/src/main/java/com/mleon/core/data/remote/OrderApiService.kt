package com.mleon.core.data.remote

import com.mleon.core.data.datasource.remote.model.OrderDto
import com.mleon.core.data.model.OrderResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface OrderApiService {
    @POST("orders")
    suspend fun createOrder(@Body request: OrderDto): OrderResult

    @GET("orders")
    suspend fun getOrders(): OrderResult
}