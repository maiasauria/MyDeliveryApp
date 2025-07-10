package com.mleon.feature.orders.viewmodel

import android.util.Log
import com.mleon.core.data.datasource.remote.model.OrderResult
import com.mleon.core.model.Order
import com.mleon.feature.orders.MainDispatcherRule
import com.mleon.feature.orders.domain.usecase.GetOrdersUseCase
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

@OptIn(ExperimentalCoroutinesApi::class)
class OrdersViewModelTest {

    private lateinit var getOrdersUseCase: GetOrdersUseCase
    private val dispatcher = StandardTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        // Mockeamos el log para que no de error en las pruebas
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.d(any(), any(), any()) } returns 0
    }

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
        coEvery { getOrdersUseCase() } returns OrderResult.SuccessList(orders)
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