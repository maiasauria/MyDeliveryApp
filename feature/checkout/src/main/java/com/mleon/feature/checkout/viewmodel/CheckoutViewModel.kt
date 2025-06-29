package com.mleon.feature.checkout.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mleon.core.data.model.OrderRequest
import com.mleon.core.data.repository.interfaces.OrderRepository
import com.mleon.core.model.dtos.CartItemDto
import com.mleon.core.model.enums.PaymentMethod
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID.randomUUID
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val repository: OrderRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState

    private val exceptionHandler =
        CoroutineExceptionHandler { _, exception ->
            Log.e("CheckoutViewModel", "Coroutine error", exception)
            _uiState.update { it.copy(errorMessage = "Ocurrió un error inesperado. Intenta nuevamente.") }
        }

    fun confirmOrder(
        cartItems: List<CartItemDto>,
        shippingAddress: String,
        paymentMethod: PaymentMethod,
        total: Double,
    ) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            Log.d(
                "CheckoutViewModel", "confirmOrder called with cartItems: $cartItems, shippingAddress: $shippingAddress, paymentMethod: $paymentMethod, total: $total",
            )

            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val request =
                    OrderRequest(
                        orderId = randomUUID().toString(),
                        productIds = cartItems,
                        shippingAddress = shippingAddress,
                        paymentMethod = paymentMethod.apiValue,
                        total = total,
                        timestamp = System.currentTimeMillis(),
                    )

                val result = repository.createOrder(request)
                Log.d("CheckoutViewModel", "Order created successfully: $result")
                if (result != null) {
                    _uiState.update { it.copy(isLoading = false, orderConfirmed = true) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message) }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    // Todavia no está en uso, falta validar TJ
    fun onPaymentMethodSelection(paymentMethod: PaymentMethod) {
        Log.d("CheckoutViewModel", "onPaymentMethodSelection called with paymentMethod: $paymentMethod",)
        _uiState.update { it.copy(paymentMethod = paymentMethod, validOrder = true) }
    }
}
