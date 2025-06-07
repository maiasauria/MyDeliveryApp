package com.mleon.mydeliveryapp.view.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.mleon.core.model.Product
import androidx.lifecycle.viewModelScope
import com.mleon.core.model.Categories
import com.mleon.mydeliveryapp.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel
@Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    private var allProducts: List<Product> = emptyList()

    private val _productState = MutableStateFlow(
        ProductListState(
            //en caso de q no haya internet
            products = listOf(
                Product(1, "Name 1", "Description 1", 10.0, true, "", listOf(Categories.PIZZA)),
            )
        )
    )
    val productState = _productState.asStateFlow()

    val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("Error occurred: ${exception.message}")
    }

    init {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            try {
                val nuevaLista = productRepository.getProducts()
                allProducts = nuevaLista // Store the fetched products
                Log.d("ProductListViewModel", "Fetched products: $nuevaLista")
                _productState.update {
                    it.copy(
                        products = nuevaLista, isLoading = false
                    )
                }

            } catch (e: Exception) {
                _productState.update {
                    it.copy(
                        error = e, isLoading = false
                    )
                }

            }
        }
    }


    fun onSearchTextChanged(query: String) {
        _productState.update { state ->
            val newState = state.copy(searchQuery = query)
            newState.copy(products = filterProducts(newState))
        }
    }

    fun onCategorySelected(category: Categories?) {
        _productState.update { state ->
            val newState = state.copy(selectedCategory = category?.name ?: "")
            newState.copy(products = filterProducts(newState))
        }
    }

    fun orderByPriceAscending() {
        _productState.update { state ->
            val sortedProducts = state.products.sortedBy { it.price }
            state.copy(products = sortedProducts)
        }
    }

    fun orderByPriceDescending() {
        _productState.update { state ->
            val sortedProducts = state.products.sortedByDescending { it.price }
            state.copy(products = sortedProducts)
        }
    }

    private fun filterProducts(state: ProductListState): List<Product> {
        return allProducts.filter { product ->
            val matchesCategory = state.selectedCategory.isBlank() ||
                    product.category.any { it.name == state.selectedCategory }
            val matchesQuery = state.searchQuery.isBlank() ||
                    product.name.contains(state.searchQuery, ignoreCase = true) ||
                    product.description.contains(state.searchQuery, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }
}


