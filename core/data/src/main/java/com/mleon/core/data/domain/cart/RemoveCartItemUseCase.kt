package com.mleon.core.data.domain.cart

import com.mleon.core.data.repository.interfaces.CartItemRepository
import javax.inject.Inject

class RemoveCartItemUseCase @Inject constructor(
    private val cartItemRepository: CartItemRepository
) {
    suspend operator fun invoke(productId: String) {
        val existingCartItem = cartItemRepository.getCartItemByProductId(productId)
        if (existingCartItem != null) {
            cartItemRepository.deleteCartItem(existingCartItem.id)
        }
    }
}