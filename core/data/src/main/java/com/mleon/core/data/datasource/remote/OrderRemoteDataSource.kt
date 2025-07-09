package com.mleon.core.data.datasource.remote

import com.mleon.core.data.datasource.OrderDataSource
import com.mleon.core.data.datasource.remote.model.OrderDto
import com.mleon.core.data.model.OrderResult
import com.mleon.core.data.remote.OrderApiService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class OrderRemoteDataSource
@Inject
constructor(
    private val apiService: OrderApiService,
) : OrderDataSource {
    override suspend fun getOrders(): OrderResult =
        try {
            apiService.getOrders()
        } catch (e: IOException) {
            OrderResult.Error("Network error: ${e.localizedMessage}")
        } catch (e: HttpException) {
            OrderResult.Error("HTTP error: ${e.code()}", e.code())
        } catch (e: Exception) {
            OrderResult.Error("Unknown error: ${e.localizedMessage}")
        }

    override suspend fun createOrder(request: OrderDto): OrderResult =
        try {
            apiService.createOrder(request)
        } catch (e: IOException) {
            OrderResult.Error("Network error: ${e.localizedMessage}")
        } catch (e: HttpException) {
            OrderResult.Error("HTTP error: ${e.code()}", e.code())
        } catch (e: Exception) {
            OrderResult.Error("Unknown error: ${e.localizedMessage}")
        }
}