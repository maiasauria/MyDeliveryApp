package com.mleon.core.data.datasource.local.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.mleon.core.model.CartItem

data class CartItemWithProductEntity (
    @Embedded val cartItemEntity: CartItemEntity,
    @Relation(
        parentColumn = "productId",
        entityColumn = "id")
            val productEntity: ProductEntity,
)

fun CartItemWithProductEntity.toCartItem(): CartItem {
    return CartItem(
        product = productEntity.toProduct(),
        quantity = cartItemEntity.quantity
    )
}