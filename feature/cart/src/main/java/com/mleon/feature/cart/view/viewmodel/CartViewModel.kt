package com.mleon.feature.cart.view.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mleon.core.data.datasource.local.entities.CartItemEntity
import com.mleon.core.data.datasource.local.entities.toCartItem
import com.mleon.core.data.repository.interfaces.CartItemRepository
import com.mleon.core.model.CartItem
import com.mleon.core.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartItemRepository: CartItemRepository,
) : ViewModel() {
    private val _cartState = MutableStateFlow(CartState()) // flujo que puede ser modificado
    val cartState = _cartState.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
        Log.e(
            "CartViewModel",
            "GLOBAL CoroutineExceptionHandler CAUGHT: Context: $coroutineContext",
            exception
        )
        _cartState.update { it.copy(errorMessage = "Ocurrió un error inesperado. Intenta nuevamente.") }
    }

    init {
        loadCartItems()
    }

    fun addToCart(product: Product) {
        launchWithLoading {
            val existingCartItem = cartItemRepository.getCartItemByProductId(product.id)
            if (existingCartItem != null) {
                val updatedCartItem = existingCartItem.copy(quantity = existingCartItem.quantity + 1)
                cartItemRepository.updateCartItem(updatedCartItem)
            } else {
                cartItemRepository.insertCartItem(
                    // TODO esto deberia manejarlo el repositorio, no el viewmodel.
                    CartItemEntity(productId = product.id, quantity = 1),
                )
            }
            updateCartState(fetchCartItemsFromDb())
        }
    }

    fun editQuantity(
        product: Product,
        quantity: Int,
    ) {
        launchWithLoading {
            val existingCartItem = cartItemRepository.getCartItemByProductId(product.id)
            if (existingCartItem != null) {
                if (quantity > 0) {
                    val updatedCartItem = existingCartItem.copy(quantity = quantity)
                    cartItemRepository.updateCartItem(updatedCartItem)
                } else {
                    cartItemRepository.deleteCartItem(existingCartItem.id)
                }
            }
            updateCartState(fetchCartItemsFromDb())
        }
    }

    fun removeFromCart(product: Product) {
        launchWithLoading {
            val existingCartItem = cartItemRepository.getCartItemByProductId(product.id)
            if (existingCartItem != null) {
                cartItemRepository.deleteCartItem(existingCartItem.id)
            }
            updateCartState(fetchCartItemsFromDb())
        }
    }

    fun clearCart() {
        launchWithLoading {
            cartItemRepository.deleteAllCartItems()
            updateCartState(emptyList())
        }
    }

    private fun loadCartItems() {
        launchWithLoading {
            updateCartState(fetchCartItemsFromDb())
        }
    }

    // Fetch cart items from DB (suspend function)
    // No tiene try catch porque se maneja en la funcion que la invoca.
    // Tampoco abre una corutina, es suspend, se llama desde la corrutina.
    private suspend fun fetchCartItemsFromDb(): List<CartItem> {
        val cartItemsWithProducts = cartItemRepository.getAllCartItemsWithProducts()
        val items = cartItemsWithProducts.first()
        return items.map { it.toCartItem() }
    }

    // Actualiza el estado del carrito con los elementos y el precio total
    private fun updateCartState(cartItems: List<CartItem>) {
        val totalPrice = cartItems.sumOf { it.product.price * it.quantity }
        _cartState.update { state ->
            state.copy(cartItems = cartItems, totalPrice = totalPrice)
        }
    }

    // Funcion helper para lanzar corutinas con manejo de excepciones y estado de carga
    // Esta funcion se encarga de lanzar una corutina, actualizar el estado de carga y manejar excepciones.
    // Le enviamos un bloque de codigo que se ejecutara dentro de la corutina.
    // block: suspend () -> Unit es una función que no tiene parámetros ni devuelve nada.
    private fun launchWithLoading(block: suspend () -> Unit) {
        // creamos una funcion suspendida. Dispatchers especifica que esta rutina esta ehcha para procesode IO.
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            _cartState.update { it.copy(isLoading = true, errorMessage = null) }
            block()
            _cartState.update { it.copy(isLoading = false) }

        }
    }
}
