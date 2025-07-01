package com.mleon.core.data.datasource.local.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.mleon.core.model.CartItem
import com.mleon.core.model.Order

data class OrderWithItemsEntity(
    @Embedded val order: OrderEntity,
    @Relation(
        entity = OrderItemEntity::class,
        parentColumn = "id",
        entityColumn = "orderId"
    )
    val items: List<OrderItemWithProductEntity>
)

fun OrderWithItemsEntity.toModel(): Order {
    return Order(
        orderId = order.id,
        shippingAddress = order.shippingAddress?: "",
        paymentMethod = order.paymentMethod ?: "",
        total = order.totalAmount,
        timestamp = order.orderDate,
        orderItems = items.map { orderItemWithProduct ->
            CartItem(
                product = orderItemWithProduct.product.toModel(),
                quantity = orderItemWithProduct.orderItem.quantity
            )
        }
    )
}