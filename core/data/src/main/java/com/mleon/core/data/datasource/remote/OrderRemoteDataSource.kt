package com.mleon.core.data.datasource.remote

import com.mleon.core.data.datasource.OrderDataSource
import com.mleon.core.data.datasource.remote.dto.OrderDto
import com.mleon.core.data.datasource.remote.dto.toDomain
import com.mleon.core.data.datasource.remote.model.toOrder
import com.mleon.core.data.datasource.remote.service.OrderApiService
import com.mleon.core.model.result.OrderResult
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val ERROR_NO_CONNECTION =
    "Sin conexión a Internet. Verifica tu conexión e intenta nuevamente"
private const val ERROR_SERVER = "Error del servidor: "
private const val ERROR_UNEXPECTED = "Ocurrió un error inesperado"
private const val ERROR_EMPTY_ORDER_ID = "Error al crear la orden. Intenta nuevamente"

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
            OrderResult.Error(ERROR_NO_CONNECTION)
        } catch (e: HttpException) {
            OrderResult.Error("$ERROR_SERVER ${e.code()}")
        } catch (e: Exception) {
            OrderResult.Error(ERROR_UNEXPECTED)
        }

    override suspend fun createOrder(request: OrderDto): OrderResult {
        return try {
            val response = apiService.createOrder(request)
            if (response.orderId.isEmpty()) return OrderResult.Error(ERROR_EMPTY_ORDER_ID)
            OrderResult.Success(response.toOrder())
        } catch (e: IOException) {
            OrderResult.Error(ERROR_NO_CONNECTION)
        } catch (e: HttpException) {
            OrderResult.Error("$ERROR_SERVER ${e.code()}")
        } catch (e: Exception) {
            OrderResult.Error(ERROR_UNEXPECTED)
        }
    }
}