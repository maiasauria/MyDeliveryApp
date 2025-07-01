package com.mleon.core.data.datasource.fake

import com.mleon.core.data.datasource.OrderDataSource
import com.mleon.core.data.model.OrderDto
import com.mleon.core.data.model.OrderResponse
import com.mleon.core.model.CartItem
import com.mleon.core.model.Order
import com.mleon.core.model.Product
import com.mleon.core.model.dtos.toDto
import com.mleon.core.model.enums.Categories
import javax.inject.Inject

class OrderFakeDataSource
    @Inject
    constructor() : OrderDataSource {
        private val orders =
            mutableListOf<Order>(
                Order(
                    orderId = "1",
                    orderItems =
                        listOf(
                            CartItem(
                                product =
                                    Product(
                                        id = "1",
                                        name = "Hamburguesa con Papas",
                                        description = "Hamburguesa de res con papas fritas y bebida.",
                                        price = 15000.0,
                                        includesDrink = true,
                                        imageUrl = "https://img.freepik.com/free-photo/front-view-burger-stand_141793-15542.jpg",
                                        category = listOf(Categories.BURGER),
                                    ),
                                quantity = 2,
                            ),
                            CartItem(
                                product =
                                    Product(
                                        id = "2",
                                        name = "Pizza de Muzzarella",
                                        description = "Pizza de muzzarella.",
                                        price = 25000.0,
                                        includesDrink = false,
                                        imageUrl = "https://img.freepik.com/free-photo/pepperoni-pizza-table_140725-5373.jpg",
                                        category = listOf(Categories.PIZZA, Categories.VEGETARIAN),
                                    ),
                                quantity = 1,
                            ),
                        ),
                    shippingAddress = "123 Main St, City",
                    paymentMethod = "Credit Card",
                    total = 49.99,
                    timestamp = System.currentTimeMillis(),
                ),
                Order(
                    orderId = "2",
                    orderItems =
                        listOf(
                            CartItem(
                                product =
                                    Product(
                                        id = "2",
                                        name = "Pizza de Muzzarella",
                                        description = "Pizza de muzzarella.",
                                        price = 25000.0,
                                        includesDrink = false,
                                        imageUrl = "https://img.freepik.com/free-photo/pepperoni-pizza-table_140725-5373.jpg",
                                        category = listOf(Categories.PIZZA, Categories.VEGETARIAN),
                                    ),
                                quantity = 1,
                            ),
                        ),
                    shippingAddress = "456 Elm St, Town",
                    paymentMethod = "PayPal",
                    total = 19.99,
                    timestamp = System.currentTimeMillis() - 86400000, // 1 day ago
                ),
            )

        override suspend fun getOrders(): List<Order> = orders

        override suspend fun createOrder(request: OrderDto): OrderResponse {
            val newOrder =
                Order(
                    orderId = "3",
                    orderItems =
                        listOf(
                            CartItem(
                                product =
                                    Product(
                                        id = "2",
                                        name = "Pizza de Muzzarella",
                                        description = "Pizza de muzzarella.",
                                        price = 25000.0,
                                        includesDrink = false,
                                        imageUrl = "https://img.freepik.com/free-photo/pepperoni-pizza-table_140725-5373.jpg",
                                        category = listOf(Categories.PIZZA, Categories.VEGETARIAN),
                                    ),
                                quantity = 1,
                            ),
                        ),
                    shippingAddress = "456 Elm St, Town",
                    paymentMethod = "PayPal",
                    total = 19.99,
                    timestamp = System.currentTimeMillis() - 86400000, // 1 day ago
                )
            orders.add(newOrder)
            return OrderResponse(
                orderId = newOrder.orderId,
                productIds = newOrder.orderItems.map { it.toDto() },
                total = newOrder.total,
                timestamp = newOrder.timestamp,
            )
        }
    }