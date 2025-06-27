package com.mleon.core.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mleon.core.data.datasource.local.dao.CartItemDao
import com.mleon.core.data.datasource.local.dao.CategoryDao
import com.mleon.core.data.datasource.local.dao.OrderDao
import com.mleon.core.data.datasource.local.dao.OrderItemDao
import com.mleon.core.data.datasource.local.dao.ProductDao
import com.mleon.core.data.datasource.local.entities.CartItemEntity
import com.mleon.core.data.datasource.local.entities.CategoryEntity
import com.mleon.core.data.datasource.local.entities.OrderEntity
import com.mleon.core.data.datasource.local.entities.OrderItemEntity
import com.mleon.core.data.datasource.local.entities.ProductCategoryCrossRef
import com.mleon.core.data.datasource.local.entities.ProductEntity

@Database(
    entities = [
        ProductEntity::class,
        OrderEntity::class,
        OrderItemEntity::class,
        ProductCategoryCrossRef::class,
        CartItemEntity::class,
    CategoryEntity::class
    ],
    version = 1,
    exportSchema = false,
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    abstract fun orderDao(): OrderDao

    abstract fun orderItemDao(): OrderItemDao

    abstract fun cartItemDao(): CartItemDao

    abstract fun categoryDao(): CategoryDao

    companion object {
        const val DATABASE_NAME = "yapp_database"
    }
}
