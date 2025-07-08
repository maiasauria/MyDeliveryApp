package com.mleon.feature.productlist.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mleon.feature.productlist.usecase.GetProductsUseCase
import com.mleon.core.model.Product
import com.mleon.core.model.enums.Categories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
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

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.e("CheckoutViewModel", "Coroutine error", exception)
        _uiState.value = ProductListUiState.Error(exception as? Exception ?: Exception("Ocurrió un error inesperado. Intenta nuevamente."))
    }

    fun loadProducts() {
        _uiState.value = ProductListUiState.Loading
        viewModelScope.launch(dispatcher + exceptionHandler) {
            try {
                allProducts = getProductsUseCase()
                //Log.d("ProductListViewModel", "Products loaded: ${allProducts.size}")

                if (allProducts.isEmpty()) {
                    _uiState.value = ProductListUiState.Error(Exception("No se encontraron productos."))
                } else {
                    updateSuccessState()
                }
            } catch (e: Exception) {
                Log.e("ProductListViewModel", "Error loading products", e)
                _uiState.value = ProductListUiState.Error(e)
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
        //Log.d("ProductListViewModel", "Updating success state with searchQuery: $searchQuery, selectedCategory: $selectedCategory, cartMessage: $cartMessage")
        val filteredProducts = filterProducts()
        _uiState.value = ProductListUiState.Success(
            products = filteredProducts,
            searchQuery = searchQuery,
            selectedCategory = selectedCategory,
            cartMessage = cartMessage
        )
    }
}
