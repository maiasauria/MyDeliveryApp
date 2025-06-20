package com.mleon.mydeliveryapp.viewmodel

import androidx.lifecycle.ViewModel
import com.mleon.core.model.Product
import androidx.lifecycle.viewModelScope
import com.mleon.core.model.Categories
import com.mleon.mydeliveryapp.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
            products = listOf(
                Product(1, "", "", 0.0, true, "", listOf()),
            )
        )
    )
    val productState = _productState.asStateFlow()

    val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("Error occurred: ${exception.message}")
    }

    init {
        _productState.update { it.copy(isLoading = true) }

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            try {
                delay(2000) // Simulate network delay
                val nuevaLista = productRepository.getProducts()
                allProducts = nuevaLista // Store the fetched products
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

    fun onSearchTextChange(query: String) {
        _productState.update { state ->
            val newState = state.copy(searchQuery = query)
            newState.copy(products = filterProducts(newState))
        }
    }

    fun onCategorySelection(category: Categories?) {
        _productState.update { state ->
            val newState = state.copy(selectedCategory = category)
            newState.copy(products = filterProducts(newState))
        }
    }

    fun onOrderByPriceAscending() {
        _productState.update { state ->
            val sortedProducts = state.products.sortedBy { it.price }
            state.copy(products = sortedProducts)
        }
    }

    fun onOrderByPriceDescending() {
        _productState.update { state ->
            val sortedProducts = state.products.sortedByDescending { it.price }
            state.copy(products = sortedProducts)
        }
    }

    private fun filterProducts(state: ProductListState): List<Product> {
        return allProducts.filter { product ->
            val matchesCategory = state.selectedCategory == null ||
                    product.category.any { it == state.selectedCategory }
            val matchesQuery = state.searchQuery.isBlank() ||
                    product.name.contains(state.searchQuery, ignoreCase = true) ||
                    product.description.contains(state.searchQuery, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }

    fun onAddToCartButtonClick(product: Product) {
        // Handle the action when the "Agregar al carrito" button is clicked
        // This could involve adding the product to a cart or showing a confirmation message
        println("Product added to cart: ${product.name}")
        _productState.update { state ->
            state.copy(
                cartMessage = "Producto ${product.name} agregado al carrito"
            )
        }
    }

    fun clearCartMessage() {
        _productState.update { state ->
            state.copy(cartMessage = "")
        }
    }

}