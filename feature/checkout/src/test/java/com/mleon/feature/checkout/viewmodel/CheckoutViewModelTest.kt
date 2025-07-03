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