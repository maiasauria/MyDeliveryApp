package com.mleon.core.data.datasource.remote

import com.mleon.core.data.datasource.OrderDataSource
import com.mleon.core.data.datasource.remote.dto.OrderDto
import com.mleon.core.data.datasource.remote.dto.toDomain
import com.mleon.core.data.datasource.remote.model.OrderResult
import com.mleon.core.data.datasource.remote.model.toOrder
import com.mleon.core.data.datasource.remote.service.OrderApiService
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
            val ordersDto = apiService.getOrders()
            val orders = ordersDto.map { it.toDomain() }
            OrderResult.SuccessList(orders)
        } catch (e: IOException) {
            OrderResult.Error("Sin conexión a Internet. Verifica tu conexión e intenta nuevamente")
        } catch (e: HttpException) {
            OrderResult.Error("Error del servidor: ${e.code()}")
        } catch (e: Exception) {
            OrderResult.Error("Ocurrió un error inesperado")
        }

    override suspend fun createOrder(request: OrderDto): OrderResult {
        return try {
            val response = apiService.createOrder(request)
            if (response.orderId.isEmpty()) {
                return OrderResult.Error("Error al crear la orden. Intenta nuevamente")
            }
            OrderResult.Success(response.toOrder())
        } catch (e: IOException) {
            OrderResult.Error("Sin conexión a Internet. Verifica tu conexión e intenta nuevamente")
        } catch (e: HttpException) {
            OrderResult.Error("Error del servidor: ${e.code()}")
        } catch (e: Exception) {
            OrderResult.Error("Ocurrió un error inesperado")
        }
    }
}