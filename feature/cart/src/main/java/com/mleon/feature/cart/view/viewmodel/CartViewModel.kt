package com.mleon.feature.cart.view.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mleon.core.data.datasource.local.entities.CartItemEntity
import com.mleon.core.data.repository.interfaces.CartItemRepository
import com.mleon.core.model.CartItem
import com.mleon.core.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
 private val cartItemRepository: CartItemRepository
) : ViewModel() {
    private val _cartState =
        MutableStateFlow(CartState()) // MutableStateFlow es un flujo que puede ser modificado
    val cartState = _cartState.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.e("CartViewModel", "Coroutine error", exception)
        _cartState.update { it.copy(errorMessage = "Ocurrió un error inesperado. Intenta nuevamente.") }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) { //creamos una funcion suspendida. Dispatchers especifica que esta rutina esta ehcha para procesode IO.
            _cartState.update { it.copy(isLoading = true) }

            val existingCartItem = cartItemRepository.getCartItemByProductId(product.id)
            if(existingCartItem != null) {
                // Si el producto ya está en el carrito, incrementamos la cantidad
                val updatedCartItem = existingCartItem.copy(quantity = existingCartItem.quantity + 1)
                cartItemRepository.updateCartItem(updatedCartItem)
            } else {
                // Si no está en el carrito, lo agregamos
                cartItemRepository.insertCartItem(CartItemEntity(productId = product.id, quantity = 1))
            }

            try {
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
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            _cartState.update { it.copy(isLoading = true) }
            try {
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
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            try {
                _cartState.update { it.copy(isLoading = true) }
                _cartState.update { state ->
                    val updatedList = state.cartItems.filter { it.product != product }
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

    fun clearCart() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            _cartState.update { it.copy(isLoading = true) }
            try {
                _cartState.update { CartState() } // Resetea el estado del carrito
            } catch (e: Exception) {
                _cartState.update { it.copy(errorMessage = e.message) }
            } finally {
                _cartState.update { it.copy(isLoading = false) }
            }
        }
    }

}