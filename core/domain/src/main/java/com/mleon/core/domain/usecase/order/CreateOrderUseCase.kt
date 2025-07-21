package com.mleon.core.domain.usecase.order

import com.mleon.core.data.repository.interfaces.OrderRepository
import com.mleon.core.model.Order
import com.mleon.core.model.result.OrderResult
import javax.inject.Inject

class CreateOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(order: Order): OrderResult {
        return orderRepository.createOrder(order)
    }
}
