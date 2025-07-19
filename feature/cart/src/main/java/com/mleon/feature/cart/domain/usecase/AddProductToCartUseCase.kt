package com.mleon.feature.cart.domain.usecase

import com.mleon.core.data.datasource.local.entities.CartItemEntity
import com.mleon.core.data.repository.interfaces.CartItemRepository
import com.mleon.core.model.Product
import javax.inject.Inject

class AddProductToCartUseCase @Inject constructor(
    private val cartItemRepository: CartItemRepository
) {
    suspend operator fun invoke(product: Product, quantity: Int = 1) {
        // Verificamos si el producto ya existe en el carrito
        val existingCartItem = cartItemRepository.getCartItemByProductId(product.id)

        // Si el producto ya está en el carrito, actualizamos la cantidad
        if (existingCartItem != null) {
            val updatedCartItem = existingCartItem.copy(quantity = quantity)
            cartItemRepository.updateCartItem(updatedCartItem)

        // Si el producto no está en el carrito, lo insertamos como un nuevo artículo
        } else {
            cartItemRepository.insertCartItem(
                CartItemEntity(productId = product.id, quantity = quantity)
            )
        }
    }
}
