package com.mleon.feature.checkout.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mleon.core.navigation.NavigationRoutes
import com.mleon.feature.cart.view.viewmodel.CartViewModel
import com.mleon.feature.checkout.viewmodel.CheckoutViewModel

@Composable
fun CheckoutScreen(
    navController: NavHostController,
    cartViewModel: CartViewModel = hiltViewModel(),
    checkoutViewModel: CheckoutViewModel = hiltViewModel(),
) {
    val uiState by checkoutViewModel.uiState.collectAsState()

    LaunchedEffect(uiState.orderConfirmed) {
        if (uiState.orderConfirmed) {
            cartViewModel.clearCart()
            navController.navigate(NavigationRoutes.ORDERS)
        }
    }

    CheckoutView(
        cartItems = uiState.cartItems,
        subtotalAmount = uiState.subTotalAmount,
        shippingCost = uiState.shippingCost,
        totalAmount = uiState.totalAmount,
        isOrderValid = uiState.validOrder,
        shippingAddress = uiState.shippingAddress,
        paymentMethod = uiState.paymentMethod,
        onConfirmOrder = checkoutViewModel::confirmOrder,
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
    )
}
