package com.mleon.feature.cart.view

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mleon.core.navigation.NavigationRoutes
import com.mleon.feature.cart.viewmodel.CartUiState
import com.mleon.feature.cart.viewmodel.CartViewModel
import com.mleon.utils.ui.ErrorScreen
import com.mleon.utils.ui.YappFullScreenLoadingIndicator

@Composable
fun CartScreen(
    navController: NavHostController,
    cartViewModel: CartViewModel = hiltViewModel()
) {
    val uiState by cartViewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        cartViewModel.loadCart()
    }

    when (uiState) {
        is CartUiState.Loading -> {
            YappFullScreenLoadingIndicator()
        }

        is CartUiState.Success -> {
            val successState = uiState as CartUiState.Success
            val cartMessage = successState.cartMessage
            LaunchedEffect(cartMessage) {
                if (cartMessage.isNotEmpty()) {
                    Toast.makeText(context, cartMessage, Toast.LENGTH_SHORT).show()
                    cartViewModel.clearCartMessage()
                }
            }

            if (successState.cartItems.isEmpty()) {
                EmptyCartView(
                    onContinueShoppingClick = { navController.navigate(NavigationRoutes.PRODUCTS) },
                )
            } else {
                CartView(
                    cartItems = successState.cartItems,
                    totalPrice = successState.total,
                    isLoading = successState.isProcessing,
                    onQuantityChange = { product, quantity ->
                        cartViewModel.editQuantity(
                            product,
                            quantity
                        )
                    },
                    onRemoveFromCart = { product -> cartViewModel.removeFromCart(product) },
                    onCheckoutClick = { navController.navigate(NavigationRoutes.CHECKOUT) }
                )
            }
        }

        is CartUiState.Error -> {
            val errorMessage = (uiState as CartUiState.Error).message
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ErrorScreen(
                    errorMessage = errorMessage,
                    onRetry = { cartViewModel.loadCart() }
                )
            }
        }
    }
}

