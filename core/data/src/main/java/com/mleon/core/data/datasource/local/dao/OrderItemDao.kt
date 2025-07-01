package com.mleon.core.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mleon.core.data.datasource.local.entities.OrderItemEntity
import com.mleon.core.data.datasource.local.entities.OrderItemWithProductEntity

@Dao
interface OrderItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItem(orderItem: OrderItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItems(orderItems: List<OrderItemEntity>)

    @Update
    suspend fun updateOrderItem(orderItem: OrderItemEntity)

    @Query("DELETE FROM order_items WHERE id = :orderItemId")
    suspend fun deleteOrderItem(orderItemId: String)

    @Query("SELECT * FROM order_items WHERE id = :orderItemId")
    suspend fun getOrderItemById(orderItemId: String): OrderItemEntity?

    @Query("SELECT * FROM order_items")
    suspend fun getAllOrderItems(): List<OrderItemEntity>

    @Transaction
    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    fun getOrderItemsWithProduct(orderId: Long): List<OrderItemWithProductEntity>
}