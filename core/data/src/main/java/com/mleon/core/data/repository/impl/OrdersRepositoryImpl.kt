package com.mleon.core.data.repository.impl

import com.mleon.core.data.datasource.OrderDataSource
import com.mleon.core.data.datasource.local.dao.OrderDao
import com.mleon.core.data.datasource.local.dao.OrderItemDao
import com.mleon.core.data.datasource.local.entities.toModel
import com.mleon.core.data.datasource.local.entities.toOrderItemEntity
import com.mleon.core.data.model.OrderApiResponse
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
    
    override suspend fun createOrder(order: Order): Order? =
        withContext(Dispatchers.IO) {

            // Convierto Order a Entidad de Room y lo guardo en la base de datos
            val orderEntity = order.toEntity()
            orderDao.insertOrder(orderEntity)

            // Convierto los OrderItems a Entidades de Room y los guardo en la base de datos
            val orderItems = order.orderItems.map { it.toOrderItemEntity(order.orderId) }
            orderItemDao.insertOrderItems(orderItems)

            // Llamo al DataSource para crear la orden en el servidor
            val orderRequest = order.toDto()
            orderDataSource.createOrder(orderRequest)

            // Si la creación fue exitosa, devuelvo el Order de Room
            val orderWithItems = orderDao.getOrderItemsWithProductById(order.orderId)
            orderWithItems?.toModel()
        }

    override suspend fun getOrderById(orderId: String): Order? {
        TODO("Not yet implemented")
    }

    override suspend fun updateOrder(order: Order): Order? {
        TODO("Not yet implemented")
    }

    override suspend fun deleteOrder(orderId: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getOrders(): List<Order> =
        withContext(Dispatchers.IO) {
            // orderDataSource.getOrders()
            orderDao.getAllOrdersWithItems().map { it.toModel() }
        }

}