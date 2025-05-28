package com.mleon.mydeliveryapp.view.ui.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mleon.core.model.Product
import com.mleon.mydeliveryapp.data.repository.ProductRepositoryImpl
import androidx.lifecycle.viewModelScope
import com.mleon.mydeliveryapp.data.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductListViewModel(
    private val repository: ProductRepository = ProductRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableLiveData<ProductListUiState>()
    val uiState: LiveData<ProductListUiState> = _uiState
    private val _userEmail = MutableLiveData<String>()
    val userEmail: LiveData<String> = _userEmail

    fun reloadProducts() {
        _uiState.value = ProductListUiState.Loading
        viewModelScope.launch {
            try {
                val products = repository.getProducts()
                _uiState.value = ProductListUiState.Success(products)
            } catch (e: Exception) {
                _uiState.value = ProductListUiState.Error(e.message ?: "Error")
            }
        }
    }

    fun searchProducts(query: String) {
        _uiState.value = ProductListUiState.Loading
        viewModelScope.launch {
            try {
                val products = repository.filterProducts(query)
                _uiState.value = ProductListUiState.Success(products)
            } catch (e: Exception) {
                _uiState.value = ProductListUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun onSearchTextChanged(query: String) {
        if (query.isEmpty()) {
            reloadProducts()
        } else {
            searchProducts(query)
        }
    }

    sealed class ProductListUiState {
        object Loading : ProductListUiState()
        data class Success(val products: List<Product>) : ProductListUiState()
        data class Error(val message: String) : ProductListUiState()
    }

    fun loadUserEmail(sharedPref: SharedPreferences) {
        _userEmail.value = sharedPref.getString("email", "") ?: ""
    }
}