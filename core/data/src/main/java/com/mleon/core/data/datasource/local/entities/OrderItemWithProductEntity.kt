package com.mleon.core.data.datasource.local.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.mleon.core.model.OrderItem

// Representa un OrderItem con su producto asociado

data class OrderItemWithProductEntity(
    @Embedded val orderItem: OrderItemEntity,
    @Relation(
        parentColumn = "productId",
        entityColumn = "id"
    )
    val product: ProductEntity
)

// Mapper
fun OrderItemWithProductEntity.toModel(): OrderItem {
    return OrderItem(
        product = product.toModel(),
        quantity = orderItem.quantity
    )
}

