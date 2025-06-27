package com.mleon.core.data.datasource.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class CartItemWithProductEntity (
    @Embedded val cartItemEntity: CartItemEntity,
    @Relation(
        parentColumn = "productId",
        entityColumn = "id")

            val productEntity: ProductEntity,
)

