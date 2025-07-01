package com.mleon.feature.productlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mleon.core.data.domain.GetProductsUseCase
import com.mleon.core.model.Product
import com.mleon.core.model.enums.Categories
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
@Inject
constructor(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {
    private var allProducts: List<Product> = emptyList()

    private val _productState = MutableStateFlow(ProductListState(products = listOf()))
    val productState = _productState.asStateFlow()

    private val exceptionHandler =
        CoroutineExceptionHandler { _, exception ->
            println("Error occurred: ${exception.message}")
        }

    init {
        _productState.update { it.copy(isLoading = true) }

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            try {

                // Traigo los productos desde el UseCase
                allProducts = getProductsUseCase() // Actualizo la lista completa de productos
                _productState.update {
                    it.copy(
                        products = allProducts, // Inicializo la lista de productos con todos los productos
                        isLoading = false,
                    )
                }
            } catch (e: Exception) {
                _productState.update {
                    it.copy(
                        error = e,
                        isLoading = false,
                    )
                }
            }
        }
    }

    fun onSearchTextChange(query: String) {
        _productState.update { state ->
            state.copy(
                searchQuery = query,
                products = filterProducts()
            )
        }
    }

    fun onCategorySelection(category: Categories?) {
        _productState.update { state ->
            state.copy(
                products = filterProducts(), //No le paso parametros porque ya los tengo en el estado
                selectedCategory = category
            )
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
            val sortedProducts = state.products.sortedByDescending { it.price } // Solo ordena los productos filtrados
            state.copy(products = sortedProducts)
        }
    }


    private fun filterProducts(
    ): List<Product> {
        val category = _productState.value.selectedCategory
        val searchQuery = _productState.value.searchQuery

        return allProducts.filter { product ->

            // Verifica si el producto pertenece a la categoría seleccionada y si coincide con la búsqueda
            val matchesCategory = category == null || product.category.any { it == category }

            // Verifica si el producto coincide con la consulta de búsqueda
            val matchesQuery = searchQuery.isBlank() ||
                    product.name.contains(searchQuery, ignoreCase = true) ||
                    product.description.contains(searchQuery, ignoreCase = true)

            // Retorna true (producto se incluye en la lista) si cumple con ambas condiciones
            matchesCategory && matchesQuery
        }
    }

    fun onAddToCartButtonClick(product: Product) {
        println("Product added to cart: ${product.name}")
        _productState.update { state ->
            state.copy(
                cartMessage = "${product.name} agregado al carrito",
            )
        }
    }

    fun clearCartMessage() {
        _productState.update { state ->
            state.copy(cartMessage = "")
        }
    }
}
