package com.mleon.feature.checkout.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mleon.core.data.model.OrderRequest
import com.mleon.core.data.repository.interfaces.OrdersRepository
import com.mleon.core.model.CartItemDto
import com.mleon.core.model.PaymentMethod
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID.randomUUID
import javax.inject.Inject


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
        paymentMethod: PaymentMethod,
        total: Double
    ) {
        viewModelScope.launch {
            Log.d("CheckoutViewModel", "confirmOrder called with cartItems: $cartItems, shippingAddress: $shippingAddress, paymentMethod: $paymentMethod, total: $total",)

            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val request = OrderRequest(
                    orderId = randomUUID().toString(),
                    productIds = cartItems,
                    shippingAddress = shippingAddress,
                    paymentMethod = paymentMethod.apiValue,
                    total = total,
                    timestamp = System.currentTimeMillis()
                )

                //TODO revisar.
                val result = repository.createOrder(request)
                Log.d("CheckoutViewModel", "Order created successfully: $result")
                // TODO revisar los codigos de retorno
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

    fun onPaymentMethodSelection(paymentMethod: PaymentMethod) {
        Log.d("CheckoutViewModel", "onPaymentMethodSelection called with paymentMethod: $paymentMethod")
        _uiState.update { it.copy(paymentMethod = paymentMethod, validOrder = true) }
    }

}


//TODO limpiar el carrito despues de confirmar.