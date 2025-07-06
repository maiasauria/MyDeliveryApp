package com.mleon.feature.checkout.viewmodel

import com.mleon.core.data.model.OrderResponse
import com.mleon.core.model.CartItem
import com.mleon.core.model.Order
import com.mleon.feature.cart.domain.usecase.GetCartItemsWithProductsUseCase
import com.mleon.feature.checkout.domain.usecase.CreateOrderUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class CheckoutViewModelTest {
    private lateinit var createOrderUseCase: CreateOrderUseCase
    private lateinit var getCartItemsWithProductsUseCase: GetCartItemsWithProductsUseCase

    @Before
    fun setUp() {
        createOrderUseCase = mockk()
        getCartItemsWithProductsUseCase = mockk()
    }

    @Test
    fun `uiState is Loading when confirmOrder is called`() = runTest {
        givenCartItems(listOf(mockCartItem()))
        givenOrderSuccess(mockOrder())

        val viewModel = CheckoutViewModel(getCartItemsWithProductsUseCase, createOrderUseCase, StandardTestDispatcher(testScheduler))
        viewModel.confirmOrder()
        Assert.assertTrue(viewModel.uiState.value is CheckoutUiState.Loading)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `uiState is Success when confirmOrder succeeds`() = runTest {
        val cartItems = listOf(mockCartItem())
        givenCartItems(cartItems)
        givenOrderSuccess(mockOrder())

        val viewModel = CheckoutViewModel(getCartItemsWithProductsUseCase, createOrderUseCase, StandardTestDispatcher(testScheduler))

        viewModel.getCartItems()
        advanceUntilIdle()
        runCurrent()
        println("After getCartItems: ${viewModel.uiState.value}")

        viewModel.confirmOrder()
        advanceUntilIdle()
        runCurrent()
        thenUiStateIsSuccess(viewModel, cartItems.size)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `uiState is Error when confirmOrder fails`() = runTest {
        val cartItems = listOf(mockCartItem())
        givenCartItems(cartItems)
        givenOrderError("Order failed")

        val viewModel = CheckoutViewModel(getCartItemsWithProductsUseCase, createOrderUseCase, StandardTestDispatcher(testScheduler))

        viewModel.getCartItems()
        advanceUntilIdle()

        assert(viewModel.uiState.value is CheckoutUiState.Success)

        viewModel.confirmOrder()
        advanceUntilIdle()

        thenUiStateIsError(viewModel, "Order failed")
    }

    @Test
    fun `uiState is Error when getCartItems fails`() = runTest {
        coEvery { getCartItemsWithProductsUseCase() } throws Exception("Cart error")
        val viewModel = CheckoutViewModel(getCartItemsWithProductsUseCase, createOrderUseCase, StandardTestDispatcher(testScheduler))
        viewModel.getCartItems()
        advanceUntilIdle()
        assert(viewModel.uiState.value is CheckoutUiState.Error)
        Assert.assertEquals("Cart error", (viewModel.uiState.value as CheckoutUiState.Error).error.message)
    }

    @Test
    fun `uiState is Error when confirmOrder throws exception`() = runTest {
        val cartItems = listOf(mockCartItem())
        givenCartItems(cartItems)
        coEvery { createOrderUseCase(any()) } throws Exception("Exception in confirmOrder")
        val viewModel = CheckoutViewModel(getCartItemsWithProductsUseCase, createOrderUseCase, StandardTestDispatcher(testScheduler))
        viewModel.getCartItems()
        advanceUntilIdle()
        viewModel.confirmOrder()
        advanceUntilIdle()
        assert(viewModel.uiState.value is CheckoutUiState.Error)
        Assert.assertEquals("Exception in confirmOrder", (viewModel.uiState.value as CheckoutUiState.Error).error.message)
    }

    @Test
    fun `onPaymentMethodSelection updates payment method in Success state`() = runTest {
        val cartItems = listOf(mockCartItem())
        givenCartItems(cartItems)
        givenOrderSuccess(mockOrder())
        val viewModel = CheckoutViewModel(getCartItemsWithProductsUseCase, createOrderUseCase, StandardTestDispatcher(testScheduler))
        viewModel.getCartItems()
        advanceUntilIdle()
        viewModel.onPaymentMethodSelection(com.mleon.core.model.enums.PaymentMethod.CREDIT_CARD)
        val state = viewModel.uiState.value
        assert(state is CheckoutUiState.Success)
        Assert.assertEquals(com.mleon.core.model.enums.PaymentMethod.CREDIT_CARD, (state as CheckoutUiState.Success).paymentMethod)
    }

    @Test
    fun `onPaymentMethodSelection does nothing if not Success state`() = runTest {
        val viewModel = CheckoutViewModel(getCartItemsWithProductsUseCase, createOrderUseCase, StandardTestDispatcher(testScheduler))
        viewModel.onPaymentMethodSelection(com.mleon.core.model.enums.PaymentMethod.CREDIT_CARD)
        assert(viewModel.uiState.value is CheckoutUiState.Loading)
    }

    @Test
    fun `confirmOrder does nothing if not Success state`() = runTest {
        val viewModel = CheckoutViewModel(getCartItemsWithProductsUseCase, createOrderUseCase, StandardTestDispatcher(testScheduler))
        viewModel.confirmOrder()
        // Should remain in Loading state
        assert(viewModel.uiState.value is CheckoutUiState.Loading)
    }

    private fun givenCartItems(cartItems: List<CartItem>) {
        coEvery { getCartItemsWithProductsUseCase() } returns cartItems
    }

    private fun givenOrderSuccess(order: Order) {
        coEvery { createOrderUseCase(any()) } returns OrderResponse.Success(order)
    }

    private fun givenOrderError(message: String) {
        coEvery { createOrderUseCase(any()) } returns OrderResponse.Error(message)
    }

    private fun mockCartItem(): CartItem = mockk(relaxed = true)
    private fun mockOrder(): Order = mockk(relaxed = true)
    private fun thenUiStateIsSuccess(viewModel: CheckoutViewModel, expectedCount: Int) {
        val state = viewModel.uiState.value
        println("Actual state: $state")
        assert(state is CheckoutUiState.Success)
    }
    private fun thenUiStateIsError(viewModel: CheckoutViewModel, expectedMessage: String) {
        val state = viewModel.uiState.value
        println("Actual state: $state")
        assert(state is CheckoutUiState.Error)
        Assert.assertEquals(expectedMessage, (state as CheckoutUiState.Error).error.message)
    }


}