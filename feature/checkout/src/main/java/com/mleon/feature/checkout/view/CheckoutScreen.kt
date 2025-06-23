package com.mleon.feature.checkout.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mleon.feature.cart.view.viewmodel.CartViewModel
import com.mleon.feature.checkout.viewmodel.CheckoutViewModel

@Composable
fun CheckoutScreen(
    navController: NavHostController,
    cartViewModel: CartViewModel, //Esto esta bien?
    checkoutViewModel: CheckoutViewModel = hiltViewModel()
) {
    val uiState by checkoutViewModel.uiState.collectAsState()

    val cartState = cartViewModel.cartState.collectAsState()
    val cartItems = cartState.value.cartItems.toList()
    val subTotalAmount = cartItems.sumOf { it.product.price * it.quantity }
    val shippingCost = 10.0 // Temporary fixed shipping cost
    val totalAmount = subTotalAmount + shippingCost // Total amount calculation
    val shippingAddress = "Calle123" // Placeholder for shipping address
    val paymentMethod = "Tarjeta de Crédito" // Placeholder for payment method

    CheckoutView(
        //TODO: pass the real values
        cartItems = cartItems,
        subtotalAmount = subTotalAmount,
        shippingCost = shippingCost,
        totalAmount = totalAmount,
        shippingAddress = shippingAddress,
        paymentMethod = paymentMethod,
        onConfirmOrder = { checkoutViewModel.confirmOrder() },
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        onNavigateToOrders = {
            //TODO: implement navigation to orders screen
        }
    )
}