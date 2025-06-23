package com.mleon.feature.checkout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mleon.core.data.model.CartItemDto
import com.mleon.core.data.model.OrderRequest
import com.mleon.core.data.repository.OrdersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID.randomUUID
import javax.inject.Inject

data class CheckoutUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val orderConfirmed: Boolean = false,
)

@HiltViewModel
class CheckoutViewModel
    @Inject
    constructor(
        private val repository: OrdersRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(CheckoutUiState())
        val uiState: StateFlow<CheckoutUiState> = _uiState

        fun confirmOrder(
            cartItems: List<CartItemDto>,
            shippingAddress: String,
            paymentMethod: String,
            total: Double
        ) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                try {
                    val request = OrderRequest( //TODO Paso el objeto o las partes?
                        orderId = randomUUID().toString(), //TODO ??
                        cartItems = cartItems,
                        shippingAddress = shippingAddress,
                        paymentMethod = paymentMethod,
                        total = total,
                        timestamp = System.currentTimeMillis()
                    )

                    //TODO revisar.
                    val result = repository.createOrder(request)
                    if (result != null) {
                        _uiState.update { it.copy(isLoading = false, orderConfirmed = true) }
                    }
                    //TODO si el pedido esta ok navegar ahistoria de pedidos o mostrar mensaje de éxito
                    //TODO validar la respuesta del servidor y manejar errores si es necesario

                } catch (e: Exception) {
                    _uiState.update { it.copy(errorMessage = e.message) }
                } finally {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

//TODO limpiar el carrito despues de confirmar.