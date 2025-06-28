package com.mleon.core.data.datasource.remote.model

import com.mleon.core.model.CartItem
import com.mleon.core.model.Order
import com.mleon.core.model.Product
import com.mleon.core.model.enums.Categories

data class RemoteOrder(
    val orderId: String,
    val productIds: List<RemoteCartItem>,
    val total: Double,
    val shippingAddress: String,
    val paymentMethod: String,
    val timestamp: Long,
)

data class RemoteCartItem(
    val _id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Double,
    val includesDrink: Boolean,
    val categories: List<String>?,
    val quantity: Int,
)

fun RemoteCartItem.toCartItem(): CartItem {
    val safeCategory =
        categories?.mapNotNull { runCatching { Categories.valueOf(it) }.getOrNull() } ?: emptyList()

    return CartItem(
        Product(
            id = _id,
            name = name,
            description = description,
            imageUrl = imageUrl,
            price = price,
            includesDrink = includesDrink,
            category = safeCategory,
        ),
        quantity = quantity,
    )
}

fun RemoteOrder.toOrder(): Order =
    Order(
        orderId = orderId,
        productIds = productIds.map { it.toCartItem() },
        shippingAddress = shippingAddress,
        paymentMethod = paymentMethod,
        total = total,
        timestamp = timestamp,
    )
