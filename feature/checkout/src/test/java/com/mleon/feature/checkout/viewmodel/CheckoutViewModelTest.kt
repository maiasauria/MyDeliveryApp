package com.mleon.feature.checkout.viewmodel

import android.util.Log
import com.mleon.core.data.datasource.remote.model.OrderResult
import com.mleon.core.model.CartItem
import com.mleon.core.model.Order
import com.mleon.core.model.enums.PaymentMethod
import com.mleon.feature.cart.domain.usecase.GetCartItemsWithProductsUseCase
import com.mleon.feature.checkout.domain.usecase.CreateOrderUseCase
import com.mleon.feature.checkout.domain.usecase.GetUserAddressUseCase
import com.mleon.feature.checkout.domain.usecase.NoAddressException
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CheckoutViewModelTest {
    private lateinit var createOrderUseCase: CreateOrderUseCase
    private lateinit var getCartItemsWithProductsUseCase: GetCartItemsWithProductsUseCase
    private lateinit var getUserAddressUseCase: GetUserAddressUseCase

    @Before
    fun setUp() {
        createOrderUseCase = mockk()
        getCartItemsWithProductsUseCase = mockk()
        getUserAddressUseCase = mockk()
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.d(any(), any(), any()) } returns 0
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `uiState is Success when confirmOrder succeeds`() = runTest {
        givenMockCartItems()
        givenOrderSuccess()
        givenUserAddress()

        val viewModel = CheckoutViewModel(
            getCartItemsWithProductsUseCase,
            createOrderUseCase,
            getUserAddressUseCase,
            StandardTestDispatcher(testScheduler)
        )

        viewModel.loadCheckoutData()
        advanceUntilIdle()
        runCurrent()

        viewModel.confirmOrder()
        advanceUntilIdle()
        runCurrent()
        thenUiStateIsSuccess(viewModel)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `uiState is Error when confirmOrder fails`() = runTest {
        givenMockCartItems()
        givenOrderError()
        givenUserAddress()

        val viewModel = CheckoutViewModel(
            getCartItemsWithProductsUseCase,
            createOrderUseCase,
            getUserAddressUseCase,
            StandardTestDispatcher(testScheduler)
        )

        viewModel.loadCheckoutData()
        advanceUntilIdle()

        assert(viewModel.uiState.value is CheckoutUiState.Success)

        viewModel.confirmOrder()
        advanceUntilIdle()

        thenUiStateIsError(viewModel)
    }

    @Test
    fun `uiState is Error when loadCheckoutData fails`() = runTest {
        coEvery { getCartItemsWithProductsUseCase() } throws Exception("Cart error")
        givenUserAddress()
        val viewModel = CheckoutViewModel(
            getCartItemsWithProductsUseCase,
            createOrderUseCase,
            getUserAddressUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.loadCheckoutData()
        advanceUntilIdle()
        assert(viewModel.uiState.value is CheckoutUiState.Error)
        Assert.assertEquals("Cart error", (viewModel.uiState.value as CheckoutUiState.Error).errorMessage)
    }

    @Test
    fun `uiState is Error when confirmOrder throws exception`() = runTest {
        givenMockCartItems()
        coEvery { createOrderUseCase(any()) } throws Exception("Error al confirmar el pedido")
        givenUserAddress()
        val viewModel = CheckoutViewModel(
            getCartItemsWithProductsUseCase,
            createOrderUseCase,
            getUserAddressUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.loadCheckoutData()
        advanceUntilIdle()
        viewModel.confirmOrder()
        advanceUntilIdle()
        thenUiStateIsError(viewModel)
    }

    @Test
    fun `onPaymentMethodSelection updates payment method in Success state`() = runTest {
        givenMockCartItems()
        givenOrderSuccess()
        givenUserAddress()

        val viewModel = CheckoutViewModel(
            getCartItemsWithProductsUseCase,
            createOrderUseCase,
            getUserAddressUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.loadCheckoutData()
        advanceUntilIdle()
        viewModel.onPaymentMethodSelection(PaymentMethod.CREDIT_CARD)

        thenUiStateIsSuccess(viewModel)
        Assert.assertEquals(PaymentMethod.CREDIT_CARD, (viewModel.uiState.value as CheckoutUiState.Success).paymentMethod)
    }

    @Test
    fun `onPaymentMethodSelection does nothing if not Success state`() = runTest {
        val viewModel = CheckoutViewModel(
            getCartItemsWithProductsUseCase,
            createOrderUseCase,
            getUserAddressUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.onPaymentMethodSelection(PaymentMethod.CREDIT_CARD)
        thenUiStateIsLoading(viewModel)
    }

    @Test
    fun `confirmOrder does nothing if not Success state`() = runTest {
        val viewModel = CheckoutViewModel(
            getCartItemsWithProductsUseCase,
            createOrderUseCase,
            getUserAddressUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.confirmOrder()
        // Deberia quedarse en Loading
        thenUiStateIsLoading(viewModel)
    }

    @Test
    fun `uiState is Success but validOrder is false when cart is empty`() = runTest {
        coEvery { getCartItemsWithProductsUseCase() } returns emptyList()
        givenUserAddress()
        val viewModel = CheckoutViewModel(
            getCartItemsWithProductsUseCase,
            createOrderUseCase,
            getUserAddressUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.loadCheckoutData()
        advanceUntilIdle()
        val state = viewModel.uiState.value as CheckoutUiState.Success
        Assert.assertFalse(state.validOrder)
    }

    @Test
    fun `uiState is MissingAddress when NoAddressException is thrown`() = runTest {
        coEvery { getUserAddressUseCase() } throws NoAddressException()
        val viewModel = CheckoutViewModel(
            getCartItemsWithProductsUseCase,
            createOrderUseCase,
            getUserAddressUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.loadCheckoutData()
        advanceUntilIdle()

        thenUiStateIsMissingAddress(viewModel)
    }

    private fun givenMockCartItems() {
        coEvery { getCartItemsWithProductsUseCase() } returns listOf(mockCartItem())
    }

    private fun givenOrderSuccess() {
        coEvery { createOrderUseCase(any()) } returns OrderResult.Success(mockOrder())
    }

    private fun givenOrderError() {
        coEvery { createOrderUseCase(any()) } returns
                OrderResult.Error("Error al confirmar el pedido")
    }

    private fun givenUserAddress() {
        coEvery { getUserAddressUseCase() } returns "Av Corrientes 123"
    }

    private fun givenEmptyUserAddress() {
        coEvery { getUserAddressUseCase() } returns ""
    }

    private fun mockCartItem(): CartItem = mockk(relaxed = true)
    private fun mockOrder(): Order = mockk(relaxed = true)

    private fun thenUiStateIsSuccess(viewModel: CheckoutViewModel) {
        val state = viewModel.uiState.value
        assert(state is CheckoutUiState.Success)
    }
    private fun thenUiStateIsLoading(viewModel: CheckoutViewModel) {
        val state = viewModel.uiState.value
        assert(state is CheckoutUiState.Loading)
    }

    private fun thenUiStateIsMissingAddress(viewModel: CheckoutViewModel) {
        val state = viewModel.uiState.value
        assert(state is CheckoutUiState.MissingAddress)
    }

    private fun thenUiStateIsError(viewModel: CheckoutViewModel) {
        val state = viewModel.uiState.value
        assert(state is CheckoutUiState.Error)
        Assert.assertEquals("Error al confirmar el pedido",
            (state as CheckoutUiState.Error).errorMessage)
    }
}