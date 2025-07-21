package com.mleon.core.domain.usecase.product

import com.mleon.core.data.repository.interfaces.CartItemRepository
import javax.inject.Inject

class GetProductQuantityInCartUseCase @Inject constructor(
    private val cartItemRepository: CartItemRepository
) {
    suspend operator fun invoke(productId: String): Int {
        // Obtenemos el artículo del carrito por el ID del producto
        val existingCartItem = cartItemRepository.getCartItemByProductId(productId)
        // Si el producto no está en el carrito, devolvemos 0
        return existingCartItem?.quantity ?: 0
    }
}
