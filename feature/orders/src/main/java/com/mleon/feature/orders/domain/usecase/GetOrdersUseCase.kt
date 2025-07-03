package com.mleon.feature.orders.domain.usecase

import com.mleon.core.data.repository.interfaces.OrderRepository
import com.mleon.core.model.Order
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(): List<Order> {
        return repository.getOrders()
    }
}