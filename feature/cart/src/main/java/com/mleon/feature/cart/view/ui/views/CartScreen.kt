package com.mleon.feature.cart.view.ui.views


import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mleon.core.navigation.NavigationRoutes
import com.mleon.feature.cart.view.viewmodel.CartViewModel

@Composable
fun CartScreen(
    navController: NavHostController,
    cartViewModel: CartViewModel = hiltViewModel()
) {
    val cartState by cartViewModel.cartState.collectAsState()
    Log.d("CartScreen", "Cart state: $cartState")
    if (cartState.cartItems.isEmpty() ) {
        EmptyCartView(
            onContinueShoppingClick = { navController.navigate("products") }
        )
    } else {
        CartView(
            state = cartState,
            onQuantityChange = { product, quantity -> cartViewModel.editQuantity(product, quantity) },
            onRemoveFromCart = { product -> cartViewModel.removeFromCart(product) },
            onCheckoutClick = { navController.navigate(NavigationRoutes.CHECKOUT) },

        )
    }

}