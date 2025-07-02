package com.mleon.feature.orders.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mleon.core.data.repository.interfaces.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val repository: OrderRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(OrdersUiState())
    val uiState: StateFlow<OrdersUiState> = _uiState

    private val exceptionHandler =
        CoroutineExceptionHandler { _, exception ->
            Log.e("OrdersViewModel", "Coroutine error", exception)
            _uiState.value = _uiState.value.copy(
                isLoading = false, // TODO vaciar la lista?
                error = "Ocurrió un error inesperado. Intenta nuevamente."
            )
        }

    fun fetchOrders() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val orders = repository.getOrders()
                _uiState.value = _uiState.value.copy(
                    orders = orders,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}