package com.mleon.core.data.repository.impl

import com.mleon.core.data.datasource.OrderDataSource
import com.mleon.core.data.datasource.local.dao.OrderDao
import com.mleon.core.data.datasource.local.dao.OrderItemDao
import com.mleon.core.data.datasource.local.entities.toEntity
import com.mleon.core.data.datasource.local.entities.toOrderItemEntity
import com.mleon.core.data.datasource.remote.dto.toDto
import com.mleon.core.model.result.OrderResult
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
    
    override suspend fun createOrder(order: Order): OrderResult =
        withContext(Dispatchers.IO) {

            // Convierto Order a Entidad de Room y lo guardo en la base de datos
            val orderEntity = order.toEntity()
            orderDao.insertOrder(orderEntity)

            // Convierto los OrderItems a Entidades de Room y los guardo en la base de datos
            val orderItems = order.orderItems.map { it.toOrderItemEntity(order.orderId) }
            orderItemDao.insertOrderItems(orderItems)

            // Llamo al DataSource para crear la orden en el servidor
            val orderRequest = order.toDto()
            val orderCreated = orderDataSource.createOrder(orderRequest)

            when (orderCreated) {
                is OrderResult.Error -> {
                    // Si hubo un error al crear la orden en el servidor,
                    // elimino la orden y los items de la base de datos
                    orderDao.deleteOrder(orderEntity.id)
                    orderItemDao.deleteOrderItemsByOrderId(order.orderId)
                    return@withContext orderCreated
                }
                else -> {
                    // Si la creación fue exitosa, retorno el resultado del DataSource
                    return@withContext orderCreated
                }
            }
        }

    override suspend fun getOrders(): OrderResult =
        withContext(Dispatchers.IO) {
            orderDataSource.getOrders()
        }
}