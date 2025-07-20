package com.mleon.feature.cart.domain.usecase

import com.mleon.core.data.repository.interfaces.CartItemRepository
import javax.inject.Inject

class ClearCartUseCase @Inject constructor(
    private val cartItemRepository: CartItemRepository
) {
    suspend operator fun invoke() {
        cartItemRepository.deleteAllCartItems()
    }
}
