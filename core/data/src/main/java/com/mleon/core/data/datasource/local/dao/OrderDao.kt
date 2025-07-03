package com.mleon.core.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mleon.core.data.datasource.local.entities.OrderEntity
import com.mleon.core.data.datasource.local.entities.OrderWithItemsEntity

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrders(orders: List<OrderEntity>)

    @Update
    suspend fun updateOrder(order: OrderEntity)

    @Query("DELETE FROM orders WHERE id = :orderId")
    suspend fun deleteOrder(orderId: String)

    @Query("SELECT * FROM orders WHERE id = :orderId")
    suspend fun getOrderById(orderId: String): OrderEntity?

    @Query("SELECT * FROM orders")
    suspend fun getAllOrders(): List<OrderEntity>

    @Transaction // Room will resolve the relation using the data class OrderWithItemsEntity
    @Query("SELECT * FROM orders")
    fun getAllOrdersWithItems(): List<OrderWithItemsEntity>

    @Transaction
    @Query("SELECT * FROM orders WHERE id = :orderId")
    suspend fun getOrderItemsWithProductById(orderId: String): OrderWithItemsEntity?
}