package com.mleon.feature.checkout.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mleon.core.model.dtos.toDto
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

    val cartState = cartViewModel.cartState.collectAsState()
    val cartItems = cartState.value.cartItems.toList()
    val subTotalAmount = cartItems.sumOf { it.product.price * it.quantity }
    val shippingCost = 10.0 // Temporary fixed shipping cost
    val totalAmount = subTotalAmount + shippingCost
    val shippingAddress = "Calle123" // TODO Reemplazar con el address del usuario


    LaunchedEffect(uiState.orderConfirmed) {
        if (uiState.orderConfirmed) {
            cartViewModel.clearCart()
            navController.navigate(NavigationRoutes.ORDERS) {
                popUpTo(NavigationRoutes.CART) { inclusive = true }
            }
        }
    }

    // TODO pasar lo que queda a State
    CheckoutView(
        cartItems = cartItems,
        subtotalAmount = subTotalAmount,
        shippingCost = shippingCost,
        totalAmount = totalAmount,
        isOrderValid = uiState.validOrder,
        shippingAddress = shippingAddress,
        paymentMethod = uiState.paymentMethod,
        onConfirmOrder = {
            checkoutViewModel.confirmOrder(
                cartItems.map { it.toDto() },
                shippingAddress,
                uiState.paymentMethod,
                totalAmount,
            )
        },
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
    )
}
