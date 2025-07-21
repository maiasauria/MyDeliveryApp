package com.mleon.feature.cart.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mleon.core.domain.usecase.cart.AddProductToCartUseCase
import com.mleon.core.domain.usecase.cart.ClearCartUseCase
import com.mleon.core.domain.usecase.cart.EditCartItemQuantityUseCase
import com.mleon.core.domain.usecase.cart.GetCartItemsWithProductsUseCase
import com.mleon.core.domain.usecase.cart.RemoveCartItemUseCase
import com.mleon.core.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val DELAY = 500L
private const val ERROR_UNEXPECTED = "Ocurrió un error inesperado. Intenta nuevamente."
private const val ERROR_ADD = "Error al agregar el producto al carrito"
private const val ERROR_EDIT = "Error al editar la cantidad del producto"
private const val ERROR_REMOVE = "Error al eliminar el producto del carrito"
private const val ERROR_CLEAR = "Error al limpiar el carrito"
private const val ERROR_LOAD = "Error al cargar el carrito"
private const val PRODUCT_ADDED_SUFFIX = " agregado al carrito"

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
        _uiState.value = CartUiState.Error(message = ERROR_UNEXPECTED)
    }

    fun addToCart(product: Product, quantity: Int = 1) {
        val state = _uiState.value
        if (state is CartUiState.Success) {
            _uiState.value = state.copy(isProcessing = true)
        }
        viewModelScope.launch(dispatcher + exceptionHandler) {
            try {
                addProductToCartUseCase(product, quantity)
                updateCartUiState("${product.name}$PRODUCT_ADDED_SUFFIX")
            } catch (e: Exception) {
                _uiState.value = CartUiState.Error(e.message ?: ERROR_ADD)
            }
        }
    }

    fun editQuantity(product: Product, quantity: Int) {
        val state = _uiState.value
        if (state is CartUiState.Success) {
            _uiState.value = state.copy(isProcessing = true)
        }
        viewModelScope.launch(dispatcher + exceptionHandler) {
            try {
                editCartItemQuantityUseCase(product, quantity)
                updateCartUiState()
            } catch (e: Exception) {
                _uiState.value = CartUiState.Error(e.message ?: ERROR_EDIT)
            }
        }
    }

    fun removeFromCart(product: Product) {
        val state = _uiState.value
        if (state is CartUiState.Success) {
            _uiState.value = state.copy(isProcessing = true)
        }
        viewModelScope.launch(dispatcher + exceptionHandler) {
            try {
                removeCartItemUseCase(product.id)
                updateCartUiState()
            } catch (e: Exception) {
                _uiState.value = CartUiState.Error(e.message ?: ERROR_REMOVE)
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
                _uiState.value = CartUiState.Error(e.message ?: ERROR_CLEAR)
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
                _uiState.value = CartUiState.Error(e.message ?: ERROR_LOAD)
            }
        }
    }

    // Actualiza el estado del carrito con los elementos y el precio total
    private suspend fun updateCartUiState(cartMessage: String = "") {
        delay(DELAY)
        try {
            val items = getCartItemsWithProductsUseCase()
            val total = items.sumOf { it.product.price * it.quantity }
            _uiState.value = CartUiState.Success(items, total, cartMessage, isProcessing = false)
        } catch (e: Exception) {
            _uiState.value = CartUiState.Error(e.message ?: ERROR_LOAD)
        }
    }
}

