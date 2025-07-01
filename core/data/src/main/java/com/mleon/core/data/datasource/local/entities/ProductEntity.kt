package com.mleon.core.data.datasource.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mleon.core.model.Product
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

fun ProductEntity.toModel(): Product {
    return Product(
        id = id,
        name = name,
        description = description,
        price = price,
        imageUrl = imageUrl,
       // includesDrink = includesDrink,
        includesDrink = false, // Assuming includesDrink is not part of ProductEntity
        category = categories
    )
}
fun Product.toProductEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        name = name,
        description = description,
        price = price,
        imageUrl = imageUrl ?: "",
        categories = category
    )
}