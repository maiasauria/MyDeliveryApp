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

private const val PRODUCT_ADDED_TO_CART_SUFFIX = " agregado al carrito"

@HiltViewModel
class ProductListViewModel
@Inject
constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    //Variables de estado interno. El State solo se actualiza para notificar a la UI que hubo cambios.
    private var allProducts: List<Product> = emptyList()

    private val _uiState = MutableStateFlow<ProductListUiState>(ProductListUiState.Loading)
    val uiState =  _uiState.asStateFlow()

    fun loadProducts(refreshData: Boolean = false) {
        setLoadingState()
        viewModelScope.launch(dispatcher) {
            handleProductResult(getProductsUseCase(refreshData))
        }
    }

    private fun setLoadingState() {
        _uiState.value = ProductListUiState.Loading
    }

    private fun handleProductResult(result: ProductResult) {
        when (result) {
            is ProductResult.Success -> {
                allProducts = result.products
                _uiState.value = ProductListUiState.Success(products = allProducts)
            }
            is ProductResult.Error -> {
                _uiState.value = ProductListUiState.Error(result.message)
            }
        }
    }

    fun onSearchTextChange(query: String) {
        updateState { state ->
            val filtered = filterProducts(allProducts, query, state.selectedCategory)
            state.copy(products = filtered, searchQuery = query)
        }
    }

    fun onCategorySelection(category: Categories?) {
        updateState { state ->
            val filtered = filterProducts(allProducts, state.searchQuery, category)
            state.copy(products = filtered, selectedCategory = category)
        }
    }

    fun onOrderByPriceAscending() = sortProducts { it.sortedBy { p -> p.price } }
    fun onOrderByPriceDescending() = sortProducts { it.sortedByDescending { p -> p.price } }
    fun onOrderByNameAscending() = sortProducts { it.sortedBy { p -> p.name } }
    fun onOrderByNameDescending() = sortProducts { it.sortedByDescending { p -> p.name } }

    fun onAddToCart(product: Product) {
        updateState { state ->
            state.copy(
                isAddingToCart = true, // Bloquea la UI mientras se agrega al carrito
                cartMessage  = "${product.name}$PRODUCT_ADDED_TO_CART_SUFFIX"
            )
        }
    }

    fun clearCartMessage() {
        updateState { state -> state.copy(cartMessage = "", isAddingToCart = false) }
    }

    private fun sortProducts(sorter: (List<Product>) -> List<Product>) {
        updateState { state -> state.copy(products = sorter(state.products)) }
    }

    private fun updateState(transform: (ProductListUiState.Success) -> ProductListUiState.Success) {
        val currentState = _uiState.value
        if (currentState is ProductListUiState.Success) {
            _uiState.value = transform(currentState)
        }
    }

    private fun filterProducts(
        products: List<Product>,
        query: String,
        category: Categories?
    ): List<Product> {
        return products.filter { product ->
            val matchesCategory = category == null || product.category.any { it == category }
            val matchesQuery = query.isBlank() ||
                    product.name.contains(query, ignoreCase = true) ||
                    product.description.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }
}
