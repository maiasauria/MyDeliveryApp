package com.mleon.feature.orders.viewmodel

import android.util.Log
import com.mleon.core.domain.usecase.order.GetOrdersUseCase
import com.mleon.core.model.Order
import com.mleon.core.model.result.OrderResult
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
class OrdersViewModelTest {

    private lateinit var getOrdersUseCase: GetOrdersUseCase

    @Before
    fun setUp() {
        getOrdersUseCase = mockk()
        // Mockeamos el log para que no de error en las pruebas
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.d(any(), any(), any()) } returns 0
    }

    @Test
    fun `when loadOrders is called then uiState is Loading `() = runTest {
        val viewModel = OrdersViewModel(getOrdersUseCase, StandardTestDispatcher(testScheduler))
        viewModel.loadOrders()
        thenUiStateIsLoading(viewModel)
    }

    @Test
    fun `given ordersList when loadOrders then uiState is Success`() = runTest {

        val orders = listOf(mockOrder("Order1"), mockOrder("Order2"))
        givenOrdersList(orders)

        val viewModel = OrdersViewModel(getOrdersUseCase,  StandardTestDispatcher(testScheduler))
        viewModel.loadOrders()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        thenUiStateIsSuccess(viewModel)
        thenOrderSizeIsCorrect(state, orders)
    }

    @Test
    fun `given orderSingle when loadOrders then uiState is Success with single order`() = runTest {

        val order = mockOrder("Order1")
        givenOrderSingle(order)

        val viewModel = OrdersViewModel(getOrdersUseCase, StandardTestDispatcher(testScheduler))
        viewModel.loadOrders()
        advanceUntilIdle()

        thenUiStateIsSuccess(viewModel)
        Assert.assertEquals(1, (viewModel.uiState.value as OrdersUiState.Success).orders.size)
    }

    @Test
    fun `given error when loadOrders then uiState is Error`() = runTest {
        givenOrdersError("Error al cargar los pedidos")

        val viewModel = OrdersViewModel(getOrdersUseCase, StandardTestDispatcher(testScheduler))
        viewModel.loadOrders()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assert(state is OrdersUiState.Error)
        thenUiStateIsError(viewModel)
    }

    @Test
    fun `given Exception whenLoadOrders then UiState Is Error`() = runTest {
        getOrdersUseCase = mockk()
        coEvery { getOrdersUseCase() } throws Exception("Error al cargar los pedidos")

        val viewModel = OrdersViewModel(getOrdersUseCase, StandardTestDispatcher(testScheduler))
        viewModel.loadOrders()

        advanceUntilIdle()
        thenUiStateIsError(viewModel)
    }

    @Test
    fun `given EmptyOrdersList when LoadOrders then UiState Is Success With EmptyList`() = runTest {

        givenOrdersList(emptyList())

        val viewModel = OrdersViewModel(getOrdersUseCase, StandardTestDispatcher(testScheduler))
        viewModel.loadOrders()
        advanceUntilIdle()

        thenUiStateIsSuccess(viewModel)
        Assert.assertTrue((viewModel.uiState.value as OrdersUiState.Success).orders.isEmpty())
    }
    // Helpers

    private fun givenOrdersList(orders: List<Order>) {
        coEvery { getOrdersUseCase() } returns OrderResult.SuccessList(orders)
    }

    private fun givenOrderSingle(order: Order) {
        coEvery { getOrdersUseCase() } returns OrderResult.Success(order)
    }

    private fun givenOrdersError(message: String) {
        coEvery { getOrdersUseCase() } returns OrderResult.Error(message)
    }

    private fun mockOrder(id: String): Order = mockk(relaxed = true) {
        every { this@mockk.orderId } returns id
    }

    private fun thenUiStateIsSuccess(viewModel: OrdersViewModel) {
        val state = viewModel.uiState.value
        assert(state is OrdersUiState.Success)
    }

    private fun thenUiStateIsLoading(viewModel: OrdersViewModel) {
        val state = viewModel.uiState.value
        assert(state is OrdersUiState.Loading)
    }

    private fun thenUiStateIsError(viewModel: OrdersViewModel) {
        val state = viewModel.uiState.value
        assert(state is OrdersUiState.Error)
        Assert.assertEquals("Error al cargar los pedidos", (state as OrdersUiState.Error).error.message)
    }

    private fun thenOrderSizeIsCorrect(state: OrdersUiState, orders: List<Order>) {
        assert(state is OrdersUiState.Success)
        Assert.assertEquals(orders.size, (state as OrdersUiState.Success).orders.size)
}
}

