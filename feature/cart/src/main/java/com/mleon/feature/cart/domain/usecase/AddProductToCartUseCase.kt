package com.mleon.feature.cart.domain.usecase

import com.mleon.core.data.datasource.local.entities.CartItemEntity
import com.mleon.core.data.repository.interfaces.CartItemRepository
import com.mleon.core.model.Product
import javax.inject.Inject

class AddProductToCartUseCase @Inject constructor(
    private val cartItemRepository: CartItemRepository
) {
    suspend operator fun invoke(product: Product) {
        val existingCartItem = cartItemRepository.getCartItemByProductId(product.id)
        if (existingCartItem != null) {
            val updatedCartItem = existingCartItem.copy(quantity = existingCartItem.quantity + 1)
            cartItemRepository.updateCartItem(updatedCartItem)
        } else {
            cartItemRepository.insertCartItem(CartItemEntity(productId = product.id, quantity = 1))
        }
    }
}


