package com.mleon.feature.cart.view.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mleon.core.model.Product
import com.mleon.feature.cart.domain.usecase.AddProductToCartUseCase
import com.mleon.feature.cart.domain.usecase.ClearCartUseCase
import com.mleon.feature.cart.domain.usecase.EditCartItemQuantityUseCase
import com.mleon.feature.cart.domain.usecase.GetCartItemsWithProductsUseCase
import com.mleon.feature.cart.domain.usecase.RemoveCartItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val addProductToCartUseCase: AddProductToCartUseCase,
    private val editCartItemQuantityUseCase: EditCartItemQuantityUseCase,
    private val clearCartUseCase: ClearCartUseCase,
    private val getCartItemsWithProductsUseCase: GetCartItemsWithProductsUseCase,
    private val removeCartItemUseCase: RemoveCartItemUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow<CartUiState>(CartUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
        Log.e(
            "CartViewModel",
            "CoroutineExceptionHandler CAUGHT: Context: $coroutineContext",
            exception
        )
        _uiState.value = CartUiState.Error(message = "Ocurrió un error inesperado. Intenta nuevamente.")

    }

    fun addToCart(product: Product, quantity: Int = 1) {
        val state = _uiState.value
        if (state is CartUiState.Success) {
            _uiState.value = state.copy(isProcessing = true) // Cambia el estado a Success con isProcessing = true
        }
        viewModelScope.launch(dispatcher + exceptionHandler) {
            try {
                addProductToCartUseCase(product, quantity)
                updateCartUiState("${product.name} agregado al carrito")
            } catch (e: Exception) {
                _uiState.value = CartUiState.Error(e.message ?: "Error al agregar el producto al carrito")
            }
        }
    }

    fun editQuantity(product: Product, quantity: Int) {
        val state = _uiState.value
        if (state is CartUiState.Success) {
            _uiState.value = state.copy(isProcessing = true) // Cambia el estado a Success con isProcessing = true
        }
        viewModelScope.launch(dispatcher + exceptionHandler) {
            try {
                editCartItemQuantityUseCase(product, quantity)
                updateCartUiState()
            } catch (e: Exception) {
                _uiState.value = CartUiState.Error(e.message ?: "Error al editar la cantidad del producto")
            }
        }
    }

    fun removeFromCart(product: Product) {
        val state = _uiState.value
        if (state is CartUiState.Success) {
            _uiState.value = state.copy(isProcessing = true) // Cambia el estado a Success con isProcessing = true
        }
        viewModelScope.launch(dispatcher + exceptionHandler) {
            try {
                removeCartItemUseCase(product.id)
                updateCartUiState()
            } catch (e: Exception) {
                _uiState.value = CartUiState.Error(e.message ?: "Error al eliminar el producto del carrito")
            }
        }
    }

    fun clearCart() {
        _uiState.value = CartUiState.Loading
        viewModelScope.launch(dispatcher + exceptionHandler) {
            try {
                clearCartUseCase()
                _uiState.value = CartUiState.Success(emptyList(), 0.0)
            } catch (e: Exception) {
                _uiState.value = CartUiState.Error(e.message ?: "Error al limpiar el carrito")
            }
        }
    }

    fun clearCartMessage() {
        val state = _uiState.value
        if (state is CartUiState.Success) {
            _uiState.value = state.copy(cartMessage = "")
        }
    }

    // Carga los elementos del carrito y calcula el precio total
    fun loadCart() {
        _uiState.value = CartUiState.Loading
        viewModelScope.launch(dispatcher + exceptionHandler) {
            try {
                val items = getCartItemsWithProductsUseCase()
                val total = items.sumOf { it.product.price * it.quantity }
                _uiState.value = CartUiState.Success(items, total)
            } catch (e: Exception) {
                _uiState.value = CartUiState.Error(e.message ?: "Error al cargar el carrito")
            }
        }
    }


    // Actualiza el estado del carrito con los elementos y el precio total
    private suspend fun updateCartUiState(cartMessage: String = "") {
        delay(500) // Simula un pequeño retraso para mostrar el estado de procesamiento
        try {
            val items = getCartItemsWithProductsUseCase()
            val total = items.sumOf { it.product.price * it.quantity }
            _uiState.value = CartUiState.Success(items, total, cartMessage, isProcessing = false)
        } catch (e: Exception) {
            _uiState.value = CartUiState.Error(e.message ?: "Error al cargar el carrito")
        }
    }
}
