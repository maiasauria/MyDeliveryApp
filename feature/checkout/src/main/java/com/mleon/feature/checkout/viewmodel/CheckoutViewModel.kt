package com.mleon.feature.checkout.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mleon.core.data.model.OrderResponse
import com.mleon.core.model.Order
import com.mleon.core.model.enums.PaymentMethod
import com.mleon.feature.cart.domain.usecase.GetCartItemsWithProductsUseCase
import com.mleon.feature.checkout.domain.usecase.CreateOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID.randomUUID
import javax.inject.Inject


@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val getCartItemsWithProductsUseCase: GetCartItemsWithProductsUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow<CheckoutUiState>(CheckoutUiState.Loading)
    val uiState= _uiState.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.e("CheckoutViewModel", "Coroutine error", exception)
        _uiState.value = CheckoutUiState.Error(
            exception as? Exception ?: Exception("Ocurrió un error inesperado. Intenta nuevamente.")
        )
    }

    fun getCartItems() {
        _uiState.value = CheckoutUiState.Loading
        viewModelScope.launch(dispatcher + exceptionHandler) {

            try {
                val cartItems = getCartItemsWithProductsUseCase()
                val subTotal = cartItems.sumOf { it.product.price * it.quantity }
                val shippingCost = 10.0 // Valor fijo temporalmente, se puede cambiar por una lógica más compleja
                val total = subTotal + shippingCost

                _uiState.value = CheckoutUiState.Success(
                    cartItems = cartItems,
                    paymentMethod = PaymentMethod.CASH,
                    shippingAddress = "Calle Falsa 123",
                    shippingCost = shippingCost,
                    totalAmount = total,
                    subTotalAmount = subTotal,
                    orderConfirmed = false,
                    validOrder = cartItems.isNotEmpty()
                )
            } catch (e: Exception) {
                _uiState.value = CheckoutUiState.Error(e)
            }
        }
    }


    fun confirmOrder() {
        val currentState = _uiState.value
        if (currentState !is CheckoutUiState.Success) return

        viewModelScope.launch(dispatcher + exceptionHandler) {
            _uiState.value = CheckoutUiState.Loading
            try {
                val order = Order(
                    orderId = randomUUID().toString(),
                    orderItems = currentState.cartItems,
                    shippingAddress = currentState.shippingAddress,
                    paymentMethod = currentState.paymentMethod.apiValue,
                    total = currentState.totalAmount,
                    timestamp = System.currentTimeMillis(),
                )

                when (val response = createOrderUseCase(order)) {
                    is OrderResponse.Success -> {
                        _uiState.value = currentState.copy(orderConfirmed = true)
                    }
                    is OrderResponse.Error -> {
                        _uiState.value = CheckoutUiState.Error(Exception(response.message))
                    }
                }
            } catch (e: Exception) {
                _uiState.value = CheckoutUiState.Error(e)
            }
        }
    }

    fun onPaymentMethodSelection(paymentMethod: PaymentMethod) {
        _uiState.update { state ->
            if (state is CheckoutUiState.Success) {
                state.copy(paymentMethod = paymentMethod, validOrder = true)
            } else {
                state
            }
        }
    }
}