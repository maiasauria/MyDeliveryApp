package com.mleon.core.data.repository.impl

import com.mleon.core.data.datasource.OrderDataSource
import com.mleon.core.data.datasource.local.dao.OrderDao
import com.mleon.core.data.datasource.local.dao.OrderItemDao
import com.mleon.core.data.datasource.local.entities.toOrderItemEntity
import com.mleon.core.data.model.OrderResponse
import com.mleon.core.data.model.toDto
import com.mleon.core.data.model.toEntity
import com.mleon.core.data.repository.interfaces.OrderRepository
import com.mleon.core.model.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val orderDataSource: OrderDataSource,
    private val orderDao: OrderDao,
    private val orderItemDao: OrderItemDao
) : OrderRepository {
    
    override suspend fun createOrder(order: Order): OrderResponse =
        withContext(Dispatchers.IO) {

            val orderEntity = order.toEntity()
            orderDao.insertOrder(orderEntity)

            val orderItems = order.productIds.map { it.toOrderItemEntity(order.orderId) }
            orderItemDao.insertOrderItems(orderItems)

            val orderRequest = order.toDto()
            orderDataSource.createOrder(orderRequest)
        }

    override suspend fun getOrderById(orderId: String): Order? {
        TODO("Not yet implemented")
    }

    override suspend fun updateOrder(order: Order): OrderResponse {
        TODO("Not yet implemented")
    }

    override suspend fun deleteOrder(orderId: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getOrders(): List<Order> =
        withContext(Dispatchers.IO) {
            orderDataSource.getOrders()
        }

}