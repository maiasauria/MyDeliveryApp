package com.mleon.feature.productlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mleon.core.data.repository.interfaces.ProductRepository
import com.mleon.core.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ProductDetailUiState {
    object Loading : ProductDetailUiState()
    data class Success(val product: Product) : ProductDetailUiState()
    data class Error(val error: Exception) : ProductDetailUiState()
}

@HiltViewModel
class ProductDetailViewModel @Inject constructor (
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Loading)
    val uiState: StateFlow<ProductDetailUiState> = _uiState

    fun loadProduct(productId: String) {
        _uiState.value = ProductDetailUiState.Loading
        viewModelScope.launch {
            try {
                val product = repository.getProductById(productId)
                if (product != null) {
                    _uiState.value = ProductDetailUiState.Success(product)
                } else {
                    _uiState.value = ProductDetailUiState.Error(Exception("Product not found"))
                }
            } catch (e: Exception) {
                _uiState.value = ProductDetailUiState.Error(e)
            }
        }
    }
}