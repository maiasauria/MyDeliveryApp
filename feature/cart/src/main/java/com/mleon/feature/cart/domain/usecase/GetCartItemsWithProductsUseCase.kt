package com.mleon.feature.cart.domain.usecase

import com.mleon.core.data.datasource.local.entities.toCartItem
import com.mleon.core.data.repository.interfaces.CartItemRepository
import com.mleon.core.model.CartItem
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetCartItemsWithProductsUseCase @Inject constructor(
    private val cartItemRepository: CartItemRepository
) {
    suspend operator fun invoke(): List<CartItem> {
        val cartItemsWithProducts = cartItemRepository.getAllCartItemsWithProducts()
        val items = cartItemsWithProducts.first()
        return items.map { it.toCartItem() }
    }
}