package com.mleon.feature.orders.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mleon.core.domain.usecase.order.GetOrdersUseCase
import com.mleon.core.model.result.OrderResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
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

    fun loadOrders() {
        viewModelScope.launch(dispatcher) {
            _uiState.value = OrdersUiState.Loading
            try {
                when (val result = getOrdersUseCase()) {
                    is OrderResult.SuccessList -> {
                        _uiState.value = OrdersUiState.Success(result.orders)
                    }
                    is OrderResult.Success -> {
                        _uiState.value = OrdersUiState.Success(listOf(result.order))
                    }
                    is OrderResult.Error -> {
                        _uiState.value = OrdersUiState.Error(Exception(result.message))
                    }
                }
            } catch (e: Exception) {
                _uiState.value = OrdersUiState.Error(e)
            }
        }
    }
}
