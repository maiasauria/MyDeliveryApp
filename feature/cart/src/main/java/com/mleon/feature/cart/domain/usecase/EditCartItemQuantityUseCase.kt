package com.mleon.feature.cart.domain.usecase

import com.mleon.core.data.repository.interfaces.CartItemRepository
import com.mleon.core.model.Product
import javax.inject.Inject

class EditCartItemQuantityUseCase @Inject constructor(
    private val cartItemRepository: CartItemRepository
) {
    suspend operator fun invoke(product: Product, quantity: Int) {
        val existingCartItem = cartItemRepository.getCartItemByProductId(product.id)
        if (existingCartItem != null) {
            if (quantity > 0) {
                val updatedCartItem = existingCartItem.copy(quantity = quantity)
                cartItemRepository.updateCartItem(updatedCartItem)
            } else {
                cartItemRepository.deleteCartItem(existingCartItem.id)
            }
        }
    }
}
