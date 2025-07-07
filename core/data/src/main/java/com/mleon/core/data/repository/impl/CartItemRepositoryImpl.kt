package com.mleon.core.data.repository.impl

import com.mleon.core.data.datasource.local.dao.CartItemDao
import com.mleon.core.data.datasource.local.entities.CartItemEntity
import com.mleon.core.data.datasource.local.entities.CartItemWithProductEntity
import com.mleon.core.data.repository.interfaces.CartItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartItemRepositoryImpl @Inject constructor(
    private val cartItemDao: CartItemDao
) : CartItemRepository {
    override fun getAllCartItems(): Flow<List<CartItemEntity>> = cartItemDao.getAllCartItems()
    override fun getAllCartItemsWithProducts(): Flow<List<CartItemWithProductEntity>> = cartItemDao.getCartItemsWithProducts()
    override suspend fun getCartItemByProductId(productId: String): CartItemEntity? = cartItemDao.getCartItemByProductId(productId)
    override suspend fun insertCartItem(cartItem: CartItemEntity) = cartItemDao.insertCartItem(cartItem)
    override suspend fun updateCartItem(cartItem: CartItemEntity) = cartItemDao.updateCartItem(cartItem)
    override suspend fun deleteCartItem(cartItemId: Int) = cartItemDao.deleteCartItem(cartItemId)
    override suspend fun deleteAllCartItems() = cartItemDao.deleteCartItems()
    override suspend fun insertCartItems(cartItems: List<CartItemEntity>) = cartItemDao.insertCartItems(cartItems)
}