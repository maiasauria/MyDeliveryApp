package com.mleon.feature.cart.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mleon.core.model.CartItem
import com.mleon.core.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

@HiltViewModel
class CartViewModel @Inject constructor() : ViewModel() {
    private val _cartState = MutableStateFlow(CartState())
    val cartItems = _cartState.asStateFlow()

    val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("Error occurred: ${exception.message}")
    }

//    init {
//        // Se actualiza siempre que cambien los elementos del carrito
//        viewModelScope.launch {
//            _cartState.collect { items ->
//                _totalPrice.value =
//                    items.totalPrice
//            }
//        }
//    }

    fun addToCart(product: Product) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) { //creamos una funcion suspendida. Dispatchers especifica que esta rutina esta ehcha para procesode IO.
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
                _cartState.update {
                    it.copy(isLoading = false, errorMessage = e.message)
                }
            }
        }
    }

    fun editQuantity(product: Product, quantity: Int) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) { //creamos una funcion suspendida. Dispatchers especifica que esta rutina esta ehcha para procesode IO.
            try {
                delay(500) // Simula acceso a base de datos
                _cartState.update { state ->
                    val updatedList = state.cartItems.map {
                        if (it.product == product) it.copy(quantity = quantity) else it
                    }.filter { it.quantity > 0 }
                    val totalPrice = updatedList.sumOf { it.product.price * it.quantity }
                    state.copy(cartItems = updatedList, totalPrice = totalPrice)
                }
            } catch (e: Exception) {
                _cartState.update {
                    it.copy(isLoading = false, errorMessage = e.message)
                }
            }
        }
    }

    fun removeFromCart(product: Product) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) { //creamos una funcion suspendida. Dispatchers especifica que esta rutina esta ehcha para procesode IO.
            try {
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
            }
        }
    }
}