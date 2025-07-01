package com.mleon.feature.checkout.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mleon.core.data.datasource.local.entities.toCartItem
import com.mleon.core.data.repository.interfaces.CartItemRepository
import com.mleon.core.data.repository.interfaces.OrderRepository
import com.mleon.core.model.Order
import com.mleon.core.model.enums.PaymentMethod
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID.randomUUID
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val cartItemRepository: CartItemRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState

    private val exceptionHandler =
        CoroutineExceptionHandler { _, exception ->
            Log.e("CheckoutViewModel", "Coroutine error", exception)
            _uiState.update { it.copy(errorMessage = "Ocurrió un error inesperado. Intenta nuevamente.") }
        }

    init {
        getCartItems()
    }

    private fun getCartItems() {
        viewModelScope.launch {
            val cartItems = cartItemRepository.getAllCartItemsWithProducts().first().map { it.toCartItem() }
            Log.d("CheckoutViewModel", "Cart items fetched: $cartItems")

            _uiState.update {
                it.copy(
                    cartItems = cartItems,
                    validOrder = cartItems.isNotEmpty(),
                    subTotalAmount = cartItems.sumOf { it.product.price * it.quantity },
                    shippingCost = 10.0, // Fixed shipping cost
                    totalAmount = cartItems.sumOf { item -> item.product.price * item.quantity } + 10.0 // Adding shipping cost
                )
            }
            Log.d("CheckoutViewModel", "UI state updated with cart items: ${_uiState.value}")
        }
    }

    fun confirmOrder(
    ) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {

            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val cartItems = _uiState.value.cartItems
                val total = cartItems.sumOf { it.product.price * it.quantity } + _uiState.value.shippingCost // Costo de envío fijo.

                Log.d("CheckoutViewModel", "Cart items to be sent: $cartItems")
                val request =
                    Order(
                        orderId = randomUUID().toString(),
                        productIds = cartItems,
                        shippingAddress = _uiState.value.shippingAddress, // TODO: Replace with actual user address
                        paymentMethod = _uiState.value.paymentMethod.apiValue,
                        total = total,
                        timestamp = System.currentTimeMillis(),
                    )

                val result = orderRepository.createOrder(request)
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
