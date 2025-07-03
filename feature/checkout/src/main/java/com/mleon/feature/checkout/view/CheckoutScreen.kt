package com.mleon.feature.checkout.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mleon.core.navigation.NavigationRoutes
import com.mleon.feature.cart.view.viewmodel.CartViewModel
import com.mleon.feature.checkout.viewmodel.CheckoutUiState
import com.mleon.feature.checkout.viewmodel.CheckoutViewModel
import com.mleon.utils.ui.ErrorScreen
import com.mleon.utils.ui.YappLoadingIndicator

@Composable
fun CheckoutScreen(
    navController: NavHostController,
    cartViewModel: CartViewModel = hiltViewModel(),
    checkoutViewModel: CheckoutViewModel = hiltViewModel(),
) {
    val uiState by checkoutViewModel.uiState.collectAsState()

    // Only launch once when the Composable enters the composition
    LaunchedEffect(Unit) {
        checkoutViewModel.getCartItems()
    }

    when (uiState) {
        is CheckoutUiState.Success -> {
            val successState = uiState as CheckoutUiState.Success

            LaunchedEffect(successState.orderConfirmed) {
                if (successState.orderConfirmed) {
                    cartViewModel.clearCart()
                    navController.navigate(NavigationRoutes.ORDERS)
                }
            }

            CheckoutView(
                cartItems = successState.cartItems,
                subtotalAmount = successState.subTotalAmount,
                shippingCost = successState.shippingCost,
                totalAmount = successState.totalAmount,
                isOrderValid = successState.validOrder,
                shippingAddress = successState.shippingAddress,
                paymentMethod = successState.paymentMethod,
                onConfirmOrder = checkoutViewModel::confirmOrder
            )
        }
        is CheckoutUiState.Loading -> {
            YappLoadingIndicator()
        }
        is CheckoutUiState.Error -> {
            val errorState = uiState as CheckoutUiState.Error
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ErrorScreen(
                    errorMessage = (errorState.error.message ?: "Ocurrió un error inesperado. Intenta nuevamente."),
                    onRetry = { checkoutViewModel.getCartItems() }
                )
            }
        }
    }
}
