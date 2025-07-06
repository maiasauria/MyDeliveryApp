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
        viewModel = CartViewModel(
            addProductToCartUseCase,
            editCartItemQuantityUseCase,
            clearCartUseCase,
            getCartItemsWithProductsUseCase,
            removeCartItemUseCase,
            StandardTestDispatcher(testScheduler)
        )
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

    @Test
    fun `addToCart sets Error state on exception`() = runTest {
        val product = mockProduct("Burger")
        coEvery { addProductToCartUseCase(product) } throws Exception("Add error")
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
        val state = viewModel.uiState.value
        Assert.assertTrue(state is CartUiState.Error)
        Assert.assertEquals("Add error", (state as CartUiState.Error).message)
    }

    // Test: editQuantity sets Error state on exception
    @Test
    fun `editQuantity sets Error state on exception`() = runTest {
        val product = mockProduct("Burger")
        coEvery { editCartItemQuantityUseCase(product, 5) } throws Exception("Edit error")
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
        val state = viewModel.uiState.value
        Assert.assertTrue(state is CartUiState.Error)
        Assert.assertEquals("Edit error", (state as CartUiState.Error).message)
    }

    // Test: removeFromCart sets Error state on exception
    @Test
    fun `removeFromCart sets Error state on exception`() = runTest {
        val product = mockProduct("Burger")
        coEvery { removeCartItemUseCase(product.id) } throws Exception("Remove error")
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
        val state = viewModel.uiState.value
        Assert.assertTrue(state is CartUiState.Error)
        Assert.assertEquals("Remove error", (state as CartUiState.Error).message)
    }

    // Test: clearCart sets Error state on exception
    @Test
    fun `clearCart sets Error state on exception`() = runTest {
        coEvery { clearCartUseCase() } throws Exception("Clear error")
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
        val state = viewModel.uiState.value
        Assert.assertTrue(state is CartUiState.Error)
        Assert.assertEquals("Clear error", (state as CartUiState.Error).message)
    }

    // Test: clearCartMessage clears cartMessage in Success state
    @Test
    fun `clearCartMessage clears cartMessage in Success state`() = runTest {
        val product = mockProduct("Pizza")
        val items = listOf(mockCartItem(product = product))
        // Stub for loadCart
        coEvery { getCartItemsWithProductsUseCase() } returns items
        // Stub for addToCart
        coEvery { addProductToCartUseCase(product) } returns Unit
        // Stub for getCartItemsWithProductsUseCase after addToCart
        coEvery { getCartItemsWithProductsUseCase() } returns items
        viewModel = CartViewModel(
            addProductToCartUseCase,
            editCartItemQuantityUseCase,
            clearCartUseCase,
            getCartItemsWithProductsUseCase,
            removeCartItemUseCase,
            StandardTestDispatcher(testScheduler)
        )
        // Set state to Success with a message
        viewModel.loadCart()
        advanceUntilIdle()
        viewModel.addToCart(product)
        advanceUntilIdle()
        val stateWithMessage = viewModel.uiState.value as CartUiState.Success
        Assert.assertTrue(stateWithMessage.cartMessage.isNotEmpty())
        viewModel.clearCartMessage()
        val clearedState = viewModel.uiState.value as CartUiState.Success
        Assert.assertTrue(clearedState.cartMessage.isEmpty())
    }

    // Test: updateCartUiState sets Error on exception
    @Test
    fun `updateCartUiState sets Error on exception`() = runTest {
        coEvery { getCartItemsWithProductsUseCase() } throws Exception("Update error")
        val product = mockProduct("Pizza")
        coEvery { addProductToCartUseCase(product) } returns Unit
        viewModel = CartViewModel(
            addProductToCartUseCase,
            editCartItemQuantityUseCase,
            clearCartUseCase,
            getCartItemsWithProductsUseCase,
            removeCartItemUseCase,
            StandardTestDispatcher(testScheduler)
        )
        // Use reflection or expose updateCartUiState for testing, or test via addToCart
        viewModel.addToCart(mockProduct("Pizza"))
        advanceUntilIdle()
        val state = viewModel.uiState.value
        Assert.assertTrue(state is CartUiState.Error)
        Assert.assertEquals("Update error", (state as CartUiState.Error).message)
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