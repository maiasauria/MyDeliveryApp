package com.mleon.orders.viewmodel

import androidx.lifecycle.ViewModel
import com.mleon.core.model.Order
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
@HiltViewModel
class CheckoutViewModel @Inject constructor() : ViewModel()  {
    private val _checkoutState = MutableStateFlow(CheckoutState(order = Order(
        orderId = "12345",
        userId = "user_001",
        products = emptyList(),
        totalAmount = 0.0,
        shippingAddress = "123 Main St, Springfield, USA",
        shippingPrice = 5.99,
        orderDate = "2023-10-01",
        status = "Pending"
    )))
    val checkoutState = _checkoutState.asStateFlow()


}

