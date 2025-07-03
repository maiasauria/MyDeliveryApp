package com.mleon.feature.orders.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mleon.core.data.repository.interfaces.OrderRepository
import com.mleon.feature.orders.domain.usecase.GetOrdersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow<OrdersUiState>(OrdersUiState.Loading)
    val uiState: StateFlow<OrdersUiState> = _uiState

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.e("OrdersViewModel", "Coroutine error", exception)
        _uiState.value = OrdersUiState.Error(
            Exception(exception.message ?: "Ocurrió un error inesperado. Intenta nuevamente.")
        )
    }

    fun loadOrders() {
        viewModelScope.launch(dispatcher + exceptionHandler) {
            _uiState.value = OrdersUiState.Loading
            val orders = getOrdersUseCase()
            _uiState.value = OrdersUiState.Success(orders)
        }
    }
}