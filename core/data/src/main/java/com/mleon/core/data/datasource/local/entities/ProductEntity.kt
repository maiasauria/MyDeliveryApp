package com.mleon.core.data.datasource.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mleon.core.model.enums.Categories

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val categories: List<Categories>,
)
