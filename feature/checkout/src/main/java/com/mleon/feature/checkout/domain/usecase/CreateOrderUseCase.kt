package com.mleon.feature.checkout.domain.usecase

import com.mleon.core.data.datasource.remote.model.OrderResult
import com.mleon.core.data.repository.interfaces.OrderRepository
import com.mleon.core.model.Order
import javax.inject.Inject

class CreateOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(order: Order): OrderResult {
        return orderRepository.createOrder(order)
    }
}
