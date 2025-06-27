package com.mleon.core.data.datasource.local.entities

import androidx.room.Entity

@Entity(primaryKeys = ["productId", "categoryId"])
data class ProductCategoryCrossRef(
    val productId: String,
    val categoryId: String
)