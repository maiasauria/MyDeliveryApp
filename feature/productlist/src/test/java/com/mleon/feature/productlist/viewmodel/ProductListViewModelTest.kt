package com.mleon.feature.productlist.viewmodel

import android.util.Log
import com.mleon.feature.productlist.usecase.GetProductsUseCase
import com.mleon.core.model.Product
import com.mleon.core.model.enums.Categories
import com.mleon.feature.productlist.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProductListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var getProductsUseCase: GetProductsUseCase

    @Before
    fun setUp() {
        // Mockeamos el log para que no de error en las pruebas
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.d(any(), any(), any()) } returns 0
    }

    @Test
    fun `uiState is Loading when loadProducts is called`() = runTest {
        getProductsUseCase = mockk()
        val viewModel = ProductListViewModel(getProductsUseCase, StandardTestDispatcher(testScheduler))
        viewModel.loadProducts()
        // En este caso no esperamos a que termine la corrutina, para chequear que sea Loading.
        Assert.assertTrue(viewModel.uiState.value is ProductListUiState.Loading)
    }

    @Test
    fun `given products error when loadProducts then uiState is Error`() = runTest {
        givenProductsError("Error fetching products")
        val viewModel = ProductListViewModel(getProductsUseCase, StandardTestDispatcher(testScheduler))
        viewModel.loadProducts()
        advanceUntilIdle()
        thenUiStateIsError(viewModel, "Error fetching products")
    }

    @Test
    fun `given products when loadProducts then uiState is Success`() = runTest {
        givenProducts(listOf(mockProduct("Pizza"), mockProduct("Burger")))
        val viewModel = ProductListViewModel(getProductsUseCase, StandardTestDispatcher(testScheduler))
        viewModel.loadProducts()
        advanceUntilIdle()
        thenUiStateIsSuccess(viewModel, 2)
    }

    @Test
    fun `given products when search then uiState contains filtered products`() = runTest {
        givenProducts(listOf(mockProduct("Pizza"), mockProduct("Burger")))
        val viewModel = ProductListViewModel(getProductsUseCase, StandardTestDispatcher(testScheduler))
        viewModel.loadProducts()
        advanceUntilIdle()
        viewModel.onSearchTextChange("Pizza")
        thenUiStateIsSuccess(viewModel, 1)
    }

    @Test
    fun `given products when select category then uiState contains filtered products`() = runTest {
        val pizza = mockProduct("Pizza", categories = listOf(Categories.PIZZA))
        val burger = mockProduct("Burger", categories = listOf(Categories.BURGER))
        givenProducts(listOf(pizza, burger))
        val viewModel = ProductListViewModel(getProductsUseCase, StandardTestDispatcher(testScheduler))
        viewModel.loadProducts()
        advanceUntilIdle()
        viewModel.onCategorySelection(com.mleon.core.model.enums.Categories.PIZZA)
        thenUiStateIsSuccess(viewModel, 1)
    }

    @Test
    fun `given products when order by price ascending then products are sorted`() = runTest {
        val cheap = mockProduct("Cheap", price = 5.0)
        val expensive = mockProduct("Expensive", price = 10.0)
        givenProducts(listOf(expensive, cheap))
        val viewModel = ProductListViewModel(getProductsUseCase, StandardTestDispatcher(testScheduler))
        viewModel.loadProducts()
        advanceUntilIdle()
        viewModel.onOrderByPriceAscending()
        val state = viewModel.uiState.value as ProductListUiState.Success
        Assert.assertEquals("Cheap", state.products[0].name)
        Assert.assertEquals("Expensive", state.products[1].name)
    }

    @Test
    fun `given products when order by price descending then products are sorted`() = runTest {
        val cheap = mockProduct("Cheap", price = 5.0)
        val expensive = mockProduct("Expensive", price = 10.0)
        givenProducts(listOf(cheap, expensive))
        val viewModel = ProductListViewModel(getProductsUseCase, StandardTestDispatcher(testScheduler))
        viewModel.loadProducts()
        advanceUntilIdle()
        viewModel.onOrderByPriceDescending()
        val state = viewModel.uiState.value as ProductListUiState.Success
        Assert.assertEquals("Expensive", state.products[0].name)
        Assert.assertEquals("Cheap", state.products[1].name)
    }

    @Test
    fun `given products when add to cart then cart message is set`() = runTest {
        val pizza = mockProduct("Pizza")
        givenProducts(listOf(pizza))
        val viewModel = ProductListViewModel(getProductsUseCase, StandardTestDispatcher(testScheduler))
        viewModel.loadProducts()
        advanceUntilIdle()
        viewModel.onAddToCartButtonClick(pizza)
        val state = viewModel.uiState.value as ProductListUiState.Success
        Assert.assertTrue(state.cartMessage.contains("Pizza"))
    }

    @Test
    fun `given products when clear cart message then cart message is empty`() = runTest {
        val pizza = mockProduct("Pizza")
        givenProducts(listOf(pizza))
        val viewModel = ProductListViewModel(getProductsUseCase, StandardTestDispatcher(testScheduler))
        viewModel.loadProducts()
        advanceUntilIdle()
        viewModel.onAddToCartButtonClick(pizza)
        viewModel.clearCartMessage()
        val state = viewModel.uiState.value as ProductListUiState.Success
        Assert.assertTrue(state.cartMessage.isEmpty())
    }

    @Test
    fun `given products when loadProducts then uiState is Loading immediately`() = runTest {
        givenProducts(listOf())
        val viewModel = ProductListViewModel(getProductsUseCase, StandardTestDispatcher(testScheduler))
        viewModel.loadProducts()
        Assert.assertTrue(viewModel.uiState.value is ProductListUiState.Loading)
    }

    // HELPER FUNCTIONS

    private fun givenProducts(products: List<Product>) {
        getProductsUseCase = mockk()
        coEvery { getProductsUseCase() } returns products
    }

    private fun givenProductsError(message: String) {
        getProductsUseCase = mockk()
        coEvery { getProductsUseCase() } throws Exception(message)
    }

    private fun thenUiStateIsSuccess(viewModel: ProductListViewModel, expectedCount: Int) {
        val state = viewModel.uiState.value
        assert(state is ProductListUiState.Success)
        Assert.assertEquals(expectedCount, (state as ProductListUiState.Success).products.size)
    }

    private fun thenUiStateIsError(viewModel: ProductListViewModel, expectedMessage: String) {
        val state = viewModel.uiState.value
        assert(state is ProductListUiState.Error)
        Assert.assertEquals(expectedMessage, (state as ProductListUiState.Error).error.message)
    }

    private fun mockProduct(
        name: String,
        price: Double = 0.0,
        categories: List<Categories> = emptyList(),
        description: String = ""
    ): Product {
        return mockk(relaxed = true) {
            coEvery { this@mockk.name } returns name
            coEvery { this@mockk.price } returns price
            coEvery { this@mockk.category } returns categories
            coEvery { this@mockk.description } returns description
        }
    }

    val allTestProducts = listOf(
        mockProduct(
            name = "Pizza",
            price = 12.5,
            categories = listOf(Categories.PIZZA),
            description = "Delicious cheese pizza"
        ),
        mockProduct(
            name = "Burger",
            price = 8.0,
            categories = listOf(Categories.BURGER),
            description = "Juicy beef burger"
        ),
        mockProduct(
            name = "Combo Meal",
            price = 20.0,
            categories = listOf(Categories.PIZZA, Categories.BURGER),
            description = "Pizza and burger combo"
        ),
        mockProduct(
            name = "Salad",
            price = 5.0,
            categories = listOf(Categories.SALAD),
            description = "Healthy green salad"
        ),
        mockProduct(
            name = "Expensive Steak",
            price = 100.0,
            categories = listOf(Categories.BURGER),
            description = "Premium steak burger"
        ),
    )
}
