package com.mleon.core.data.datasource.local.entities

import androidx.room.Entity
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