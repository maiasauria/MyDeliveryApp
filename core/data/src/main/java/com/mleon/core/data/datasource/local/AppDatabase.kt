package com.mleon.core.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mleon.core.data.datasource.local.dao.CartItemDao
import com.mleon.core.data.datasource.local.dao.OrderDao
import com.mleon.core.data.datasource.local.dao.OrderItemDao
import com.mleon.core.data.datasource.local.dao.ProductDao
import com.mleon.core.data.datasource.local.entities.CartItemEntity
import com.mleon.core.data.datasource.local.entities.OrderEntity
import com.mleon.core.data.datasource.local.entities.OrderItemEntity
import com.mleon.core.data.datasource.local.entities.ProductEntity

@Database(
    entities = [
        ProductEntity::class,
        OrderEntity::class,
        OrderItemEntity::class,
        CartItemEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(CategoriesListConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    abstract fun orderDao(): OrderDao

    abstract fun orderItemDao(): OrderItemDao

    abstract fun cartItemDao(): CartItemDao

    companion object {
        const val DATABASE_NAME = "yapp_database"
    }
}
