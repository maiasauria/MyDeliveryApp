package com.mleon.feature.cart.view.viewmodel

import com.mleon.core.domain.usecase.cart.AddProductToCartUseCase
import com.mleon.core.domain.usecase.cart.ClearCartUseCase
import com.mleon.core.domain.usecase.cart.EditCartItemQuantityUseCase
import com.mleon.core.domain.usecase.cart.GetCartItemsWithProductsUseCase
import com.mleon.core.domain.usecase.cart.RemoveCartItemUseCase
import com.mleon.core.model.CartItem
import com.mleon.core.model.Product
import com.mleon.feature.cart.viewmodel.CartUiState
import com.mleon.feature.cart.viewmodel.CartViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {
    private val addProductToCartUseCase: AddProductToCartUseCase = mockk()
    private val editCartItemQuantityUseCase: EditCartItemQuantityUseCase = mockk()
    private val clearCartUseCase: ClearCartUseCase = mockk()
    private val getCartItemsWithProductsUseCase: GetCartItemsWithProductsUseCase = mockk()
    private val removeCartItemUseCase: RemoveCartItemUseCase = mockk()
    private lateinit var viewModel: CartViewModel

    @Test
    fun `uiState is Loading when loadCart is called`() = runTest {
        viewModel = CartViewModel(
            addProductToCartUseCase,
            editCartItemQuantityUseCase,
            clearCartUseCase,
            getCartItemsWithProductsUseCase,
            removeCartItemUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.loadCart()
        thenUiStateIsLoading(viewModel)
    }

    @Test
    fun `uiState is Success when cart items are loaded`() = runTest {
        givenCartItems(listOf(mockCartItem()))
        viewModel = CartViewModel(
            addProductToCartUseCase,
            editCartItemQuantityUseCase,
            clearCartUseCase,
            getCartItemsWithProductsUseCase,
            removeCartItemUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.loadCart()
        advanceUntilIdle()
        thenUiStateIsSuccess(viewModel)
        Assert.assertEquals(1, (viewModel.uiState.value as CartUiState.Success).cartItems.size)
    }

    @Test
    fun `uiState is Error when loadCart throws exception`() = runTest {
        givenCartItemsError("Cart error")
        viewModel = CartViewModel(
            addProductToCartUseCase,
            editCartItemQuantityUseCase,
            clearCartUseCase,
            getCartItemsWithProductsUseCase,
            removeCartItemUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.loadCart()
        advanceUntilIdle()
        thenUiStateIsError(viewModel, "Cart error")
    }

    @Test
    fun `uiState is Success with message when addToCart succeeds`() = runTest {
        val product = mockProduct("Pizza")
        givenAddToCartSuccess(product)
        givenCartItems(listOf(mockCartItem(product = product)))
        viewModel = CartViewModel(
            addProductToCartUseCase,
            editCartItemQuantityUseCase,
            clearCartUseCase,
            getCartItemsWithProductsUseCase,
            removeCartItemUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.addToCart(product)
        advanceUntilIdle()
        thenUiStateIsSuccess(viewModel)
        Assert.assertTrue((viewModel.uiState.value as CartUiState.Success).cartMessage.contains("Pizza"))
    }

    @Test
    fun `uiState is Error when addToCart throws exception`() = runTest {
        val product = mockProduct("Burger")
        givenAddToCartError(product, "Add error")
        viewModel = CartViewModel(
            addProductToCartUseCase,
            editCartItemQuantityUseCase,
            clearCartUseCase,
            getCartItemsWithProductsUseCase,
            removeCartItemUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.addToCart(product)
        advanceUntilIdle()
        thenUiStateIsError(viewModel, "Add error")
    }

    @Test
    fun `uiState is Success when editQuantity succeeds`() = runTest {
        val product = mockProduct("Pizza")
        givenEditQuantitySuccess(product, 2)
        givenCartItems(listOf(mockCartItem(product = product, quantity = 2)))
        viewModel = CartViewModel(
            addProductToCartUseCase,
            editCartItemQuantityUseCase,
            clearCartUseCase,
            getCartItemsWithProductsUseCase,
            removeCartItemUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.editQuantity(product, 2)
        advanceUntilIdle()
        thenUiStateIsSuccess(viewModel)
        Assert.assertEquals(2, (viewModel.uiState.value as CartUiState.Success).cartItems[0].quantity)
    }

    @Test
    fun `uiState is Error when editQuantity throws exception`() = runTest {
        val product = mockProduct("Burger")
        givenEditQuantityError(product, 5, "Edit error")
        viewModel = CartViewModel(
            addProductToCartUseCase,
            editCartItemQuantityUseCase,
            clearCartUseCase,
            getCartItemsWithProductsUseCase,
            removeCartItemUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.editQuantity(product, 5)
        advanceUntilIdle()
        thenUiStateIsError(viewModel, "Edit error")
    }

    @Test
    fun `uiState is Success when removeFromCart succeeds`() = runTest {
        val product = mockProduct("Pizza")
        givenRemoveFromCartSuccess(product)
        givenCartItems(emptyList())
        viewModel = CartViewModel(
            addProductToCartUseCase,
            editCartItemQuantityUseCase,
            clearCartUseCase,
            getCartItemsWithProductsUseCase,
            removeCartItemUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.removeFromCart(product)
        advanceUntilIdle()
        thenUiStateIsSuccess(viewModel)
        Assert.assertTrue((viewModel.uiState.value as CartUiState.Success).cartItems.isEmpty())
    }

    @Test
    fun `uiState is Error when removeFromCart throws exception`() = runTest {
        val product = mockProduct("Burger")
        givenRemoveFromCartError(product, "Remove error")
        viewModel = CartViewModel(
            addProductToCartUseCase,
            editCartItemQuantityUseCase,
            clearCartUseCase,
            getCartItemsWithProductsUseCase,
            removeCartItemUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.removeFromCart(product)
        advanceUntilIdle()
        thenUiStateIsError(viewModel, "Remove error")
    }

    @Test
    fun `uiState is Success with empty list when clearCart succeeds`() = runTest {
        givenClearCartSuccess()
        viewModel = CartViewModel(
            addProductToCartUseCase,
            editCartItemQuantityUseCase,
            clearCartUseCase,
            getCartItemsWithProductsUseCase,
            removeCartItemUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.clearCart()
        advanceUntilIdle()
        thenUiStateIsSuccess(viewModel)
        val state = viewModel.uiState.value as CartUiState.Success
        Assert.assertTrue(state.cartItems.isEmpty())
        Assert.assertEquals(0.0, state.total, 0.01)
    }

    @Test
    fun `uiState is Error when clearCart throws exception`() = runTest {
        givenClearCartError("Clear error")
        viewModel = CartViewModel(
            addProductToCartUseCase,
            editCartItemQuantityUseCase,
            clearCartUseCase,
            getCartItemsWithProductsUseCase,
            removeCartItemUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.clearCart()
        advanceUntilIdle()
        thenUiStateIsError(viewModel, "Clear error")
    }

    @Test
    fun `clearCartMessage clears cartMessage in Success state`() = runTest {
        val product = mockProduct("Pizza")
         givenAddToCartSuccess(product)
        givenCartItems(listOf(mockCartItem(product = product)))
        viewModel = CartViewModel(
            addProductToCartUseCase,
            editCartItemQuantityUseCase,
            clearCartUseCase,
            getCartItemsWithProductsUseCase,
            removeCartItemUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.loadCart()
        advanceUntilIdle()
        viewModel.addToCart(product)
        advanceUntilIdle()
        thenUiStateIsSuccess(viewModel)
        val stateWithMessage = viewModel.uiState.value as CartUiState.Success
        Assert.assertTrue(stateWithMessage.cartMessage.isNotEmpty())
        viewModel.clearCartMessage()
        advanceUntilIdle()
        thenUiStateIsSuccess(viewModel)
        val clearedState = viewModel.uiState.value as CartUiState.Success
        Assert.assertTrue(clearedState.cartMessage.isEmpty())
    }

    // Helpers

    private fun givenCartItems(items: List<CartItem>) {
        coEvery { getCartItemsWithProductsUseCase() } returns items
    }

    private fun givenCartItemsError(message: String) {
        coEvery { getCartItemsWithProductsUseCase() } throws Exception(message)
    }

    private fun givenAddToCartSuccess(product: Product) {
        coEvery { addProductToCartUseCase(product) } returns Unit
    }

    private fun givenAddToCartError(product: Product, message: String) {
        coEvery { addProductToCartUseCase(product) } throws Exception(message)
    }

    private fun givenEditQuantitySuccess(product: Product, quantity: Int) {
        coEvery { editCartItemQuantityUseCase(product, quantity) } returns Unit
    }

    private fun givenEditQuantityError(product: Product, quantity: Int, message: String) {
        coEvery { editCartItemQuantityUseCase(product, quantity) } throws Exception(message)
    }

    private fun givenRemoveFromCartSuccess(product: Product) {
        coEvery { removeCartItemUseCase(product.id) } returns Unit
    }

    private fun givenRemoveFromCartError(product: Product, message: String) {
        coEvery { removeCartItemUseCase(product.id) } throws Exception(message)
    }

    private fun givenClearCartSuccess() {
        coEvery { clearCartUseCase() } returns Unit
    }

    private fun givenClearCartError(message: String) {
        coEvery { clearCartUseCase() } throws Exception(message)
    }

    private fun mockProduct(name: String) = Product(
        id = name.hashCode().toString(),
        name = name,
        price = 10.0,
        category = emptyList(),
        description = "",
        imageUrl = "",
        includesDrink = true,
    )

    private fun mockCartItem(
        product: Product = mockProduct("Pizza"),
        quantity: Int = 1
    ) = CartItem(
        product = product,
        quantity = quantity
    )

    private fun thenUiStateIsSuccess(viewModel: CartViewModel) {
        val state = viewModel.uiState.value
        assert(state is CartUiState.Success)
    }

    private fun thenUiStateIsLoading(viewModel: CartViewModel) {
        val state = viewModel.uiState.value
        assert(state is CartUiState.Loading)
    }

    private fun thenUiStateIsError(viewModel: CartViewModel, message: String) {
        val state = viewModel.uiState.value
        assert(state is CartUiState.Error)
        Assert.assertEquals(message, (state as CartUiState.Error).message)
    }
}
