package com.mleon.feature.cart.view.ui.views

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current

    LaunchedEffect(cartState.errorMessage) {
        cartState.errorMessage?.let { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    if (cartState.cartItems.isEmpty()) {
        EmptyCartView(
            onContinueShoppingClick = { navController.popBackStack() }
        )
    } else {
        CartView(
            cartItems = cartState.cartItems,
            totalPrice = cartState.totalPrice,
            isLoading = cartState.isLoading,
            onQuantityChange = { product, quantity -> cartViewModel.editQuantity(product, quantity) },
            onRemoveFromCart = { product -> cartViewModel.removeFromCart(product) },
            onCheckoutClick = { navController.navigate(NavigationRoutes.CHECKOUT) }
        )
    }
}