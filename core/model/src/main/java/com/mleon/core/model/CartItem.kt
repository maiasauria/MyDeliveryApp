package com.mleon.core.model

data class CartItem(
    val product: Product,
    var quantity: Int = 1
) {
    val totalPrice: Double
        get() = product.price * quantity

    fun incrementQuantity() {
        quantity++
    }

    fun decrementQuantity() {
        if (quantity > 1) {
            quantity--
        }
    }
}