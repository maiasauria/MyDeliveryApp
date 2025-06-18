package com.mleon.feature.cart.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mleon.core.model.CartItem
import com.mleon.core.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor() : ViewModel() {
    private val _cartState = MutableStateFlow(CartState())
    val cartItems = _cartState.asStateFlow()

    val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("Error occurred: ${exception.message}")
    }

    fun addToCart(product: Product) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) { //creamos una funcion suspendida. Dispatchers especifica que esta rutina esta ehcha para procesode IO.
            _cartState.update { it.copy(isLoading = true) }
            try {
                delay(500) // Simula acceso a base de datos
                _cartState.update { state ->
                    val existingItem = state.cartItems.find { it.product == product }
                    val updatedList = if (existingItem != null) {
                        state.cartItems.map {
                            if (it.product == product) it.copy(quantity = it.quantity + 1) else it
                        }
                    } else {
                        state.cartItems + CartItem(product, 1)
                    }
                    val totalPrice = updatedList.sumOf { it.product.price * it.quantity }
                    state.copy(cartItems = updatedList, totalPrice = totalPrice)
                }
            } catch (e: Exception) {
                _cartState.update { it.copy(errorMessage = e.message) }
            } finally {
                _cartState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun editQuantity(product: Product, quantity: Int) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) { //creamos una funcion suspendida. Dispatchers especifica que esta rutina esta ehcha para procesode IO.
            _cartState.update { it.copy(isLoading = true) }
            try {
                delay(1000) // Simula acceso a base de datos
                _cartState.update { state ->
                    val updatedList = state.cartItems.map {
                        if (it.product == product) it.copy(quantity = quantity) else it
                    }.filter { it.quantity > 0 }
                    val totalPrice = updatedList.sumOf { it.product.price * it.quantity }
                    state.copy(cartItems = updatedList, totalPrice = totalPrice)
                }
            } catch (e: Exception) {
                _cartState.update { it.copy(errorMessage = e.message) }
            } finally {
                _cartState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun removeFromCart(product: Product) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) { //creamos una funcion suspendida. Dispatchers especifica que esta rutina esta ehcha para procesode IO.
            try {
                _cartState.update { it.copy(isLoading = true) }
                delay(500) // Simula acceso a base de datos
                _cartState.update { state ->
                    val updatedList = state.cartItems.filter { it.product != product }
                    val totalPrice = updatedList.sumOf { it.product.price * it.quantity }
                    state.copy(cartItems = updatedList, totalPrice = totalPrice)
                }
            } catch (e: Exception) {
                _cartState.update {
                    it.copy(isLoading = false, errorMessage = e.message)
                }
            } finally {
                _cartState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun checkout() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            _cartState.update { it.copy(isLoading = true) }
            try {
                delay(1000) // Simula proceso de pago
                _cartState.update { it.copy(cartItems = emptyList(), totalPrice = 0.0) }
            } catch (e: Exception) {
                _cartState.update { it.copy(errorMessage = e.message) }
            } finally {
                _cartState.update { it.copy(isLoading = false) }
            }
        }
    }
}