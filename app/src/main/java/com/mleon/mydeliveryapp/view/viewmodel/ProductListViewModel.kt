package com.mleon.mydeliveryapp.view.viewmodel

import androidx.lifecycle.ViewModel
import com.mleon.core.model.Product
import androidx.lifecycle.viewModelScope
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
                Product(1, "Name 1", "Description 1", 10.0, true, "", "Category 1"),
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
            state.copy(
                searchQuery = query,
                products = if (query.isBlank()) {
                    allProducts
                } else {
                    allProducts.filter { it.name.contains(query, ignoreCase = true) || it.description.contains(query, ignoreCase = true) } //TODO este filtrado va aca o en el repositorio?
                }
            )
        }
    }
}


