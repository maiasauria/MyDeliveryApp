package com.mleon.feature.productlist.viewmodel

import com.mleon.core.data.domain.GetProductsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

class ProductListViewModelTest {

    private lateinit var getProductsUseCase: GetProductsUseCase
    private lateinit var productListViewModel: ProductListViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        productListViewModel = ProductListViewModel(getProductsUseCase)
    }

    @Test
    fun `test initial state`() {
        // Verify that the initial state is correct
        assert(productListViewModel.productState.value.products.isEmpty())
        assert(!productListViewModel.uiState.value.isLoading)
        assert(productListViewModel.uiState.value.errorMessage.isEmpty())
    }
}
