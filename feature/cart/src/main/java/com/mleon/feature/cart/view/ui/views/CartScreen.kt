package com.mleon.feature.cart.view.ui.views


import androidx.compose.runtime.*

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mleon.feature.cart.view.viewmodel.CartViewModel
import android.util.Log
@Composable
fun CartScreen(
    navController: NavHostController,
    cartViewModel: CartViewModel = hiltViewModel()
) {
    val cartState by cartViewModel.cartItems.collectAsState()
    Log.d("CartScreen", "Cart state: $cartState")
    CartView(
        state = cartState,
        onQuantityChange = { product, quantity ->
            cartViewModel.editQuantity(product, quantity)
        },
        onRemoveFromCart = { product ->
            cartViewModel.removeFromCart(product)
        },
        onCheckoutClick = {
            // Navigate to checkout or show a message
            navController.navigate("checkout")
        }
    )
}