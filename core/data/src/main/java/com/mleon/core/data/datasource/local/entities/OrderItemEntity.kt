package com.mleon.core.data.datasource.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mleon.core.model.CartItem

@Entity(
    tableName = "order_items",
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["id"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.CASCADE, // Si se elimina un pedido, eliminamos los items del pedido
        ),
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.NO_ACTION, //Si se elimina un producto, no eliminamos los items del pedido
        ),
    ],

    //Indices para mejorar las consultas
    indices = [
        Index(value = ["orderId"]),
        Index(value = ["productId"])
    ]
)

data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val orderId: String,
    val productId: String,
    val quantity: Int = 1,
)


fun CartItem.toOrderItemEntity(orderId: String): OrderItemEntity {
    return OrderItemEntity(
        orderId = orderId,
        productId = this.product.id,
        quantity = this.quantity
    )
}

