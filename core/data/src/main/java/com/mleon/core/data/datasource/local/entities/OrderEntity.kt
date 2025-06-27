package com.mleon.core.data.datasource.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L, // Long porque podemos tener muchos pedidos
    val orderDate: Long = System.currentTimeMillis(),
    val totalAmount: Double = 0.0,
    val totalItems: Int = 0,
    val shippingAddress: String? = null,
    val paymentMethod: String? = null,
)

@Entity(
    tableName = "order_items",
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["id"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.NO_ACTION,
        ),
    ],
)
data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val orderId: Int,
    val productId: String,
    val quantity: Int = 1,
    val price: Double,
)
