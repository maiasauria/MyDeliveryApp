package com.mleon.core.data.repository.interfaces

import com.mleon.core.data.model.OrderResponse
import com.mleon.core.model.Order

interface OrderRepository {
    suspend fun getOrders(): List<Order>
    suspend fun createOrder(order: Order): OrderResponse
    suspend fun getOrderById(orderId: String): Order?
    suspend fun updateOrder(order: Order): OrderResponse
    suspend fun deleteOrder(orderId: String): Boolean
}