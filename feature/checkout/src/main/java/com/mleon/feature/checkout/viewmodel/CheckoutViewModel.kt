package com.mleon.feature.checkout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CheckoutUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val orderConfirmed: Boolean = false
)

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    //TODO: inject the necessary dependencies
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState

    fun confirmOrder() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                //TODO: call the repository to confirm the order
                // Simulate a successful order confirmation
                _uiState.update { it.copy(isLoading = false, orderConfirmed = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }
}