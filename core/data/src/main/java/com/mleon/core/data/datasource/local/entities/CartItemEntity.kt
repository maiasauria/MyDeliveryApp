package com.mleon.core.data.datasource.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "cart_items",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"], // se usa el id del producto
            childColumns = ["productId"], // se usa el productId del cart item
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // se genera en la app, no lo traemos del servidor
    val productId: String,
    val quantity: Int = 1,
)
