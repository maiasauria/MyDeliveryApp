package com.mleon.core.domain.usecase.order

import com.mleon.core.data.repository.interfaces.OrderRepository
import com.mleon.core.model.result.OrderResult
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(): OrderResult {
        return repository.getOrders()
    }
}
