package com.mleon.core.data.repository.interfaces

import com.mleon.core.data.datasource.local.entities.CartItemEntity
import com.mleon.core.data.datasource.local.entities.CartItemWithProductEntity
import kotlinx.coroutines.flow.Flow

interface CartItemRepository {
    fun getAllCartItems(): Flow<List<CartItemEntity>>
    fun getAllCartItemsWithProducts(): Flow<List<CartItemWithProductEntity>>
    suspend fun getCartItemByProductId(productId: String): CartItemEntity?
    suspend fun insertCartItem(cartItem: CartItemEntity)
    suspend fun updateCartItem(cartItem: CartItemEntity)
    suspend fun deleteCartItem(cartItemId: Int)
    suspend fun deleteCartItems()
    suspend fun insertCartItems(cartItems: List<CartItemEntity>)
}