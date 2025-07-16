package com.mleon.feature.checkout.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mleon.core.data.datasource.remote.model.OrderResult
import com.mleon.core.model.CartItem
import com.mleon.core.model.Order
import com.mleon.core.model.enums.PaymentMethod
import com.mleon.feature.cart.domain.usecase.GetCartItemsWithProductsUseCase
import com.mleon.feature.checkout.domain.usecase.CreateOrderUseCase
import com.mleon.feature.checkout.domain.usecase.GetUserAddressUseCase
import com.mleon.feature.checkout.domain.usecase.NoAddressException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

private const val ERROR_MESSAGE = "Ocurrió un error inesperado. Intenta nuevamente."
private const val SHIPPING_COST = 1000.0

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val getCartItemsWithProductsUseCase: GetCartItemsWithProductsUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
    private val getUserAddressUseCase: GetUserAddressUseCase,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow<CheckoutUiState>(CheckoutUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.e("CheckoutViewModel", "Coroutine error", exception)
        _uiState.value = CheckoutUiState.Error(exception.message ?: ERROR_MESSAGE)
    }

    fun loadCheckoutData() {
        _uiState.value = CheckoutUiState.Loading
        viewModelScope.launch(dispatcher + exceptionHandler) {
            try {
                val address = getUserAddressUseCase()
                if (address.isBlank()) {
                    _uiState.value = CheckoutUiState.MissingAddress
                    return@launch
                }
                val cartItems = getCartItemsWithProductsUseCase()
                val subTotal = cartItems.sumOf { it.product.price * it.quantity }
                val shippingCost = SHIPPING_COST
                val total = subTotal + shippingCost
                _uiState.value = CheckoutUiState.Success(
                    cartItems = cartItems,
                    paymentMethod = PaymentMethod.CASH, // Fijo
                    shippingAddress = address,
                    shippingCost = shippingCost,
                    totalAmount = total,
                    subTotalAmount = subTotal,
                    orderConfirmed = false,
                    validOrder = isOrderValid(cartItems, address)
                )
            } catch (e: NoAddressException) {
                _uiState.value = CheckoutUiState.MissingAddress
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }

    fun confirmOrder() = viewModelScope.launch(dispatcher + exceptionHandler) {
        val state = _uiState.value as? CheckoutUiState.Success ?: return@launch
        _uiState.value = CheckoutUiState.Loading

        if (state.shippingAddress.isBlank()) {
            _uiState.value = CheckoutUiState.MissingAddress
            return@launch
        }
        val order = Order(
            orderId = UUID.randomUUID().toString(),
            orderItems = state.cartItems,
            shippingAddress = state.shippingAddress,
            paymentMethod = state.paymentMethod.apiValue,
            total = state.totalAmount,
            timestamp = System.currentTimeMillis(),
        )
        val result = createOrderUseCase(order)
        when (result) {
            is OrderResult.Success, is OrderResult.SuccessList -> _uiState.value =
                state.copy(orderConfirmed = true)

            is OrderResult.Error -> setError(result.message)
        }
    }

    fun onPaymentMethodSelection(paymentMethod: PaymentMethod) {
        _uiState.update { state ->
            //Solo actualizamos el estado si es un CheckoutUiState.Success
            if (state is CheckoutUiState.Success) {
                state.copy(paymentMethod = paymentMethod)
            } else {
                // Si no es un estado de éxito, no hacemos nada
                state
            }
        }
    }

    private fun isOrderValid(cartItems: List<CartItem>, address: String?): Boolean {
        return cartItems.isNotEmpty() && !address.isNullOrBlank()
    }

    private fun setError(message: String?) {
        _uiState.value = CheckoutUiState.Error(message ?: ERROR_MESSAGE)
    }
}