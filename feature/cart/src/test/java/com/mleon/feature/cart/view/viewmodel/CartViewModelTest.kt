package com.mleon.feature.cart.view.viewmodel

import com.mleon.core.model.CartItem
import com.mleon.core.model.Product
import com.mleon.feature.cart.domain.usecase.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {

    private val addProductToCartUseCase: AddProductToCartUseCase = mockk()
    private val editCartItemQuantityUseCase: EditCartItemQuantityUseCase = mockk()
    private val clearCartUseCase: ClearCartUseCase = mockk()
    private val getCartItemsWithProductsUseCase: GetCartItemsWithProductsUseCase = mockk()
    private val removeCartItemUseCase: RemoveCartItemUseCase = mockk()
    private val dispatcher = StandardTestDispatcher()

    private lateinit var viewModel: CartViewModel

    @Before
    fun setup() {

    }

    @Test
    fun `uiState is Loading when loadCart is called`() = runTest {
        viewModel.loadCart()
        Assert.assertTrue(viewModel.uiState.value is CartUiState.Loading)
    }

    @Test
    fun `given cart items when loadCart then uiState is Success`() = runTest {
        val items = listOf(mockCartItem())
        coEvery { getCartItemsWithProductsUseCase() } returns items

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
        val state = viewModel.uiState.value
        Assert.assertTrue(state is CartUiState.Success)
        Assert.assertEquals(1, (state as CartUiState.Success).cartItems.size)
    }

    @Test
    fun `given error when loadCart then uiState is Error`() = runTest {
        coEvery { getCartItemsWithProductsUseCase() } throws Exception("Cart error")
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
        val state = viewModel.uiState.value
        Assert.assertTrue(state is CartUiState.Error)
        Assert.assertEquals("Cart error", (state as CartUiState.Error).message)
    }

    @Test
    fun `addToCart updates uiState to Success with message`() = runTest {
        val product = mockProduct("Pizza")
        coEvery { addProductToCartUseCase(product) } returns Unit
        coEvery { getCartItemsWithProductsUseCase() } returns listOf(mockCartItem(product = product))
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
        val state = viewModel.uiState.value as CartUiState.Success
        Assert.assertTrue(state.cartMessage.contains("Pizza"))
    }

    @Test
    fun `editQuantity updates uiState to Success`() = runTest {
        val product = mockProduct("Pizza")
        coEvery { editCartItemQuantityUseCase(product, 2) } returns Unit
        coEvery { getCartItemsWithProductsUseCase() } returns listOf(
            mockCartItem(
                product = product,
                quantity = 2
            )
        )
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
        val state = viewModel.uiState.value as CartUiState.Success
        Assert.assertEquals(2, state.cartItems[0].quantity)
    }

    @Test
    fun `removeFromCart updates uiState to Success`() = runTest {
        val product = mockProduct("Pizza")
        coEvery { removeCartItemUseCase(product.id) } returns Unit
        coEvery { getCartItemsWithProductsUseCase() } returns emptyList()
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
        val state = viewModel.uiState.value as CartUiState.Success
        Assert.assertTrue(state.cartItems.isEmpty())
    }

    @Test
    fun `clearCart sets uiState to Success with empty list`() = runTest {
        coEvery { clearCartUseCase() } returns Unit
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
        val state = viewModel.uiState.value as CartUiState.Success
        Assert.assertTrue(state.cartItems.isEmpty())
        Assert.assertEquals(0.0, state.total, 0.01)
    }

    // Helpers

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
}