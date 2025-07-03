package com.mleon.feature.checkout.domain.usecase

import com.mleon.core.data.model.OrderResponse
import com.mleon.core.data.repository.interfaces.OrderRepository
import com.mleon.core.model.Order
import javax.inject.Inject

class CreateOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(order: Order): OrderResponse {
        return try {
            val createdOrder = orderRepository.createOrder(order)
            if (createdOrder != null) {
                OrderResponse.Success(createdOrder)
            } else {
                OrderResponse.Error("Error al crear el pedido")
            }
        } catch (e: Exception) {
            OrderResponse.Error(e.message ?: "Error desconocido al crear el pedido")
        }
    }
}