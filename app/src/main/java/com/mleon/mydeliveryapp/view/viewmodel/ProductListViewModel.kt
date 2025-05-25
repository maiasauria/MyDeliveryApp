package com.mleon.mydeliveryapp.view.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mleon.mydeliveryapp.data.model.Product
import com.mleon.mydeliveryapp.data.repository.ProductRepositoryImpl
import androidx.lifecycle.viewModelScope
import com.mleon.mydeliveryapp.data.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductListViewModel(
    private val repository: ProductRepository = ProductRepositoryImpl()
) : ViewModel() {

    private val _products: MutableLiveData<List<Product>> = MutableLiveData()
    val products: LiveData<List<Product>> = _products

    fun reloadProducts() {
        viewModelScope.launch {
            _products.value = repository.getProducts()
        }
    }

    fun searchProducts(query: String) {
        viewModelScope.launch {
            _products.value = repository.filterProducts(query)
        }
    }
}