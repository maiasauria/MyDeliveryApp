package com.mleon.core.domain.usecase.cart

import com.mleon.core.data.repository.interfaces.CartItemRepository
import javax.inject.Inject

class ClearCartUseCase @Inject constructor(
    private val cartItemRepository: CartItemRepository
) {
    suspend operator fun invoke() {
        cartItemRepository.deleteAllCartItems()
    }
}
