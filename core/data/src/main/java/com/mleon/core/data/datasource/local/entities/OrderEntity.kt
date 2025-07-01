package com.mleon.core.data.datasource.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val id: String, // Se trae desde la DataSource.
    val orderDate: Long = 0,
    val totalAmount: Double = 0.0,
    val totalItems: Int = 0,
    val shippingAddress: String? = null,
    val paymentMethod: String? = null
)