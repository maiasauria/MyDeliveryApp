package com.mleon.feature.productlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mleon.core.domain.usecase.product.GetProductByIdUseCase
import com.mleon.core.domain.usecase.product.GetProductQuantityInCartUseCase
import com.mleon.core.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val ERROR_PRODUCT_NOT_FOUND = "Producto no encontrado"
private const val PRODUCT_ADDED_TO_CART_SUFFIX = " agregado al carrito"

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val getProductQuantityInCartUseCase: GetProductQuantityInCartUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Loading)
    val uiState: StateFlow<ProductDetailUiState> = _uiState

    fun loadProductDetail(productId: String) {
        _uiState.value = ProductDetailUiState.Loading
        viewModelScope.launch(dispatcher) {
            try {
                val product = getProductByIdUseCase(productId)
                val quantityInCart = getProductQuantityInCartUseCase(productId).takeIf { it > 0 } ?: 1
                if (product != null) {
                    _uiState.value = ProductDetailUiState.Success(product, quantityInCart)
                } else {
                    _uiState.value = ProductDetailUiState.Error(Exception(ERROR_PRODUCT_NOT_FOUND))
                }
            } catch (e: Exception) {
                _uiState.value = ProductDetailUiState.Error(e)
            }
        }
    }

    fun onAddToCart(product: Product) {
        val currentState = _uiState.value
        if (currentState is ProductDetailUiState.Success) {
            _uiState.value = currentState.copy(message = "${product.name}$PRODUCT_ADDED_TO_CART_SUFFIX")
        }
    }

    fun clearMessage() {
        val currentState = _uiState.value as ProductDetailUiState.Success
        if (uiState.value is ProductDetailUiState.Success) {
            _uiState.value = currentState.copy(message = "")
        }
    }

    fun updateQuantity(newQuantity: Int) {
        val currentState = _uiState.value
        if (currentState is ProductDetailUiState.Success) {
            _uiState.value = currentState.copy(quantityInCart = newQuantity)
        }
    }
}
