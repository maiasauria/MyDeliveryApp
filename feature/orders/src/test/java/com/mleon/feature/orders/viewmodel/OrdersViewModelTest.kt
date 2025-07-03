package com.mleon.feature.orders.viewmodel

import com.mleon.core.model.Order
import com.mleon.feature.orders.domain.usecase.GetOrdersUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OrdersViewModelTest {

    private lateinit var getOrdersUseCase: GetOrdersUseCase
    private val dispatcher = StandardTestDispatcher()

    @Test
    fun `uiState is Loading when loadOrders is called`() = runTest {
        getOrdersUseCase = mockk()
        val viewModel = OrdersViewModel(getOrdersUseCase, dispatcher)
        viewModel.loadOrders()
        Assert.assertTrue(viewModel.uiState.value is OrdersUiState.Loading)
    }

    @Test
    fun `given orders when loadOrders then uiState is Success`() = runTest {
        val orders = listOf(mockOrder("Order1"), mockOrder("Order2"))
        givenOrders(orders)
        val viewModel = OrdersViewModel(getOrdersUseCase,  StandardTestDispatcher(testScheduler))
        viewModel.loadOrders()
        advanceUntilIdle()
        val state = viewModel.uiState.value
        assert(state is OrdersUiState.Success)
        Assert.assertEquals(2, (state as OrdersUiState.Success).orders.size)
    }

    @Test
    fun `given error when loadOrders then uiState is Error`() = runTest {
        givenOrdersError("Failed to fetch orders")
        val viewModel = OrdersViewModel(getOrdersUseCase, StandardTestDispatcher(testScheduler))
        viewModel.loadOrders()
        advanceUntilIdle()
        val state = viewModel.uiState.value
        assert(state is OrdersUiState.Error)
        Assert.assertEquals("Failed to fetch orders", (state as OrdersUiState.Error).error.message)
    }

    // Helpers

    private fun givenOrders(orders: List<Order>) {
        getOrdersUseCase = mockk()
        coEvery { getOrdersUseCase() } returns orders
    }

    private fun givenOrdersError(message: String) {
        getOrdersUseCase = mockk()
        coEvery { getOrdersUseCase() } throws Exception(message)
    }

    private fun mockOrder(id: String): Order {
        return mockk(relaxed = true) {
            coEvery { this@mockk.orderId } returns id
        }
    }
}