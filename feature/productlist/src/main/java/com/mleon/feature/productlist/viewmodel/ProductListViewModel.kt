package com.mleon.feature.productlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mleon.core.data.datasource.remote.model.ProductResult
import com.mleon.core.model.Product
import com.mleon.core.model.enums.Categories
import com.mleon.feature.productlist.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel
@Inject
constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    //Holdear todos los productos en memoria. Se podria modificar con queries a la DB.
    //Variables de estado interno. El State solo se actualiza para notificar a la UI que hubo cambios.
    private var allProducts: List<Product> = emptyList()
    private var searchQuery: String = ""
    private var selectedCategory: Categories? = null
    private var cartMessage: String = ""

    private val _uiState = MutableStateFlow<ProductListUiState>(ProductListUiState.Loading)
    val uiState =  _uiState.asStateFlow()

    fun loadProducts(refreshData: Boolean = false) {
        _uiState.value = ProductListUiState.Loading
        viewModelScope.launch(dispatcher) {
            when (val result = getProductsUseCase(refreshData)) {
                is ProductResult.Success -> {
                    allProducts = result.products
                    updateSuccessState()
                }
                is ProductResult.Error -> {
                    _uiState.value = ProductListUiState.Error(result.message)
                }
            }
        }
    }

    fun onSearchTextChange(query: String) {
        searchQuery = query
        updateSuccessState()
    }

    fun onCategorySelection(category: Categories?) {
        selectedCategory = category
        updateSuccessState()
    }

    fun onOrderByPriceAscending() {
        val currentState = _uiState.value
        if (currentState is ProductListUiState.Success) {
            val sorted = currentState.products.sortedBy { it.price }
            _uiState.value = currentState.copy(products = sorted)
        }
    }

    fun onOrderByPriceDescending() {
        val currentState = _uiState.value
        if (currentState is ProductListUiState.Success) {
            val sorted = currentState.products.sortedByDescending { it.price }
            _uiState.value = currentState.copy(products = sorted)
        }
    }

    private fun filterProducts(): List<Product> {
        return allProducts.filter { product ->
            // Verifica si el producto pertenece a la categoría seleccionada
            val matchesCategory = selectedCategory == null || product.category.any { it == selectedCategory }
            // Verifica si el producto coincide con la consulta de búsqueda
            val matchesQuery = searchQuery.isBlank() ||
                    product.name.contains(searchQuery, ignoreCase = true) ||
                    product.description.contains(searchQuery, ignoreCase = true)
            // Retorna true (producto se incluye en la lista) si cumple con ambas condiciones
            matchesCategory && matchesQuery
        }
    }

    fun onAddToCartButtonClick(product: Product) {
        val currentState = _uiState.value
        if (currentState is ProductListUiState.Success) {
            _uiState.value = currentState.copy(isAddingToCart = true) // Bloquea la UI mientras se agrega al carrito
        }
        cartMessage = "${product.name} agregado al carrito"
        updateSuccessState()
    }

    fun clearCartMessage() {
        cartMessage = ""
        updateSuccessState()
    }

    private fun updateSuccessState() {
        val filteredProducts = filterProducts()
        _uiState.value = ProductListUiState.Success(
            products = filteredProducts,
            searchQuery = searchQuery,
            selectedCategory = selectedCategory,
            cartMessage = cartMessage
        )
    }
}
