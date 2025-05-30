package com.mleon.feature.cart.view.ui.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mleon.core.model.Product

class CartViewModel : ViewModel() {

    private val _totalPrice = MutableLiveData<Double>()
    val totalPrice: LiveData<Double> = _totalPrice

    private val _cartItems = MutableLiveData<Map<Product, Int>>()
    val cartItems: LiveData<Map<Product, Int>> = _cartItems


    fun addToCart(product: Product) {
        val currentCart = _cartItems.value ?: emptyMap()
        val newQuantity = (currentCart[product] ?: 0) + 1
        _cartItems.value = currentCart.toMutableMap().apply { put(product, newQuantity) }
        updateTotalPrice()
    }

    fun removeFromCart(product: Product) {
        val currentCart = _cartItems.value ?: emptyMap()
        val currentQuantity = currentCart[product] ?: 0
        val newCart = currentCart.toMutableMap()
        if (currentQuantity > 1) {
            newCart[product] = currentQuantity - 1
        } else {
            newCart.remove(product)
        }
        _cartItems.value = newCart
        updateTotalPrice()
    }

    fun editQuantity(product: Product, quantity: Int) {
        val currentCart = _cartItems.value ?: emptyMap()
        val newCart = currentCart.toMutableMap()
        if (quantity > 0) {
            newCart[product] = quantity
        } else {
            newCart.remove(product)
        }
        _cartItems.value = newCart
        updateTotalPrice()
    }

    private fun updateTotalPrice() {
        val cart = _cartItems.value ?: emptyMap()
        _totalPrice.value = cart.entries.sumOf { (product, quantity) -> product.price * quantity }
    }

}