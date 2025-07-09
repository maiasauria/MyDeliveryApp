package com.mleon.feature.orders.domain.usecase

import com.mleon.core.data.model.OrderResult
import com.mleon.core.data.repository.interfaces.OrderRepository
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(): OrderResult {
        return repository.getOrders()
    }
}