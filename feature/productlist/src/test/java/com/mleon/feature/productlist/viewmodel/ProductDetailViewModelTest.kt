package com.mleon.feature.productlist.viewmodel

import android.util.Log
import com.mleon.core.model.Product
import com.mleon.core.model.enums.Categories
import com.mleon.feature.productlist.usecase.GetProductByIdUseCase
import com.mleon.feature.productlist.usecase.GetProductQuantityInCartUseCase
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
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductDetailViewModelTest {
    private lateinit var getProductByIdUseCase: GetProductByIdUseCase
    private lateinit var getProductQuantityInCartUseCase: GetProductQuantityInCartUseCase

    @Before
    fun setUp() {
        getProductByIdUseCase = mockk()
        getProductQuantityInCartUseCase = mockk()
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.d(any(), any(), any()) } returns 0
    }

    @Test
    fun `uiState is Loading when loadProductDetail is called`() = runTest {
        givenProductFound(mockProduct())
        givenProductQuantity(1)
        val viewModel = ProductDetailViewModel(
            getProductByIdUseCase,
            getProductQuantityInCartUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.loadProductDetail("1")
        thenUiStateIsLoading(viewModel)
    }

    @Test
    fun `uiState is Success when product is found`() = runTest {
        val product = mockProduct()
        givenProductFound(product)
        givenProductQuantity(2)
        val viewModel = ProductDetailViewModel(
            getProductByIdUseCase,
            getProductQuantityInCartUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.loadProductDetail(product.id)
        advanceUntilIdle()
        thenUiStateIsSuccess(viewModel)
        thenProductIsCorrect(viewModel.uiState.value, product)
        thenQuantityIsCorrect(viewModel.uiState.value, 2)
    }

    @Test
    fun `uiState is Error when product is not found`() = runTest {
        givenProductNotFound()
        givenProductQuantity(0)
        val viewModel = ProductDetailViewModel(
            getProductByIdUseCase,
            getProductQuantityInCartUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.loadProductDetail("999")
        advanceUntilIdle()
        thenUiStateIsError(viewModel)
    }

    @Test
    fun `uiState is Error when exception is thrown`() = runTest {
        givenProductThrowsException("Network error")
        givenProductQuantity(0)
        val viewModel = ProductDetailViewModel(
            getProductByIdUseCase,
            getProductQuantityInCartUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.loadProductDetail("1")
        advanceUntilIdle()
        thenUiStateIsError(viewModel)
    }

    @Test
    fun `updateQuantity changes quantity in Success state`() = runTest {
        val product = mockProduct()
        givenProductFound(product)
        givenProductQuantity(1)
        val viewModel = ProductDetailViewModel(
            getProductByIdUseCase,
            getProductQuantityInCartUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.loadProductDetail(product.id)
        advanceUntilIdle()
        viewModel.updateQuantity(5)
        thenQuantityIsCorrect(viewModel.uiState.value, 5)
    }

    @Test
    fun `onAddToCart sets message in Success state`() = runTest {
        val product = mockProduct()
        givenProductFound(product)
        givenProductQuantity(1)
        val viewModel = ProductDetailViewModel(
            getProductByIdUseCase,
            getProductQuantityInCartUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.loadProductDetail(product.id)
        advanceUntilIdle()
        viewModel.onAddToCart(product)
        thenMessageContains(viewModel.uiState.value, product.name)
    }

    @Test
    fun `clearMessage sets message to empty in Success state`() = runTest {
        val product = mockProduct()
        givenProductFound(product)
        givenProductQuantity(1)
        val viewModel = ProductDetailViewModel(
            getProductByIdUseCase,
            getProductQuantityInCartUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.loadProductDetail(product.id)
        advanceUntilIdle()
        viewModel.onAddToCart(product)
        viewModel.clearMessage()
        thenMessageIsEmpty(viewModel.uiState.value)
    }

    // Helpers
    private fun givenProductFound(product: Product) {
        coEvery { getProductByIdUseCase(product.id) } returns product
    }
    private fun givenProductNotFound() {
        coEvery { getProductByIdUseCase(any()) } returns null
    }
    private fun givenProductThrowsException(message: String) {
        coEvery { getProductByIdUseCase(any()) } throws Exception(message)
    }
    private fun givenProductQuantity(quantity: Int) {
        coEvery { getProductQuantityInCartUseCase(any()) } returns quantity
    }
    private fun mockProduct(
        name: String = "Pizza",
        id: String = "1",
        price: Double = 10.0,
        categories: List<Categories> = listOf(Categories.PIZZA)) =
        Product(id, name, "desc", price,  false,  null, categories)

    private fun thenUiStateIsSuccess(viewModel: ProductDetailViewModel) {
        val state = viewModel.uiState.value
        assert(state is ProductDetailUiState.Success)
    }
    private fun thenUiStateIsLoading(viewModel: ProductDetailViewModel) {
        val state = viewModel.uiState.value
        assert(state is ProductDetailUiState.Loading)
    }
    private fun thenUiStateIsError(viewModel: ProductDetailViewModel) {
        val state = viewModel.uiState.value
        assert(state is ProductDetailUiState.Error)
    }
    private fun thenProductIsCorrect(state: ProductDetailUiState, product: Product) {
        assert(state is ProductDetailUiState.Success)
        Assert.assertEquals(product, (state as ProductDetailUiState.Success).product)
    }
    private fun thenQuantityIsCorrect(state: ProductDetailUiState, quantity: Int) {
        assert(state is ProductDetailUiState.Success)
        Assert.assertEquals(quantity, (state as ProductDetailUiState.Success).quantityInCart)
    }
    private fun thenMessageContains(state: ProductDetailUiState, text: String) {
        assert(state is ProductDetailUiState.Success)
        Assert.assertTrue((state as ProductDetailUiState.Success).message.contains(text))
    }
    private fun thenMessageIsEmpty(state: ProductDetailUiState) {
        assert(state is ProductDetailUiState.Success)
        Assert.assertTrue((state as ProductDetailUiState.Success).message.isEmpty())
    }
}
