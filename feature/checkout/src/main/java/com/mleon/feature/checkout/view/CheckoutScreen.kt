package com.mleon.feature.checkout.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mleon.core.navigation.NavigationRoutes
import com.mleon.feature.cart.viewmodel.CartViewModel
import com.mleon.feature.checkout.viewmodel.CheckoutUiState
import com.mleon.feature.checkout.viewmodel.CheckoutViewModel
import com.mleon.utils.ui.ErrorScreen
import com.mleon.utils.ui.YappFullScreenLoadingIndicator

private const val TITLE_SHIPPING_ADDRESS = "Dirección de envío"
private const val TEXT_MISSING_ADDRESS = "Necesitas indicar un domicilio para enviar tu pedido"
private const val BUTTON_EDIT_PROFILE = "Editar perfil"

@Composable
fun CheckoutScreen(
    navController: NavHostController,
    cartViewModel: CartViewModel = hiltViewModel(),
    checkoutViewModel: CheckoutViewModel = hiltViewModel(),
) {
    val uiState by checkoutViewModel.uiState.collectAsState()

    val showMissingAddressDialog = remember { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        showMissingAddressDialog.value = uiState is CheckoutUiState.MissingAddress
    }

    LaunchedEffect(Unit) {
        checkoutViewModel.loadCheckoutData()
    }

    when (uiState) {
        is CheckoutUiState.Success -> {
            val successState = uiState as CheckoutUiState.Success

            LaunchedEffect(successState.orderConfirmed) {
                if (successState.orderConfirmed) {
                    cartViewModel.clearCart()
                    navController.navigate(NavigationRoutes.ORDERS) {
                        popUpTo(NavigationRoutes.CHECKOUT) {
                            inclusive = true
                        }
                    }
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
            YappFullScreenLoadingIndicator()
        }
        is CheckoutUiState.Error -> {
            val errorState = uiState as CheckoutUiState.Error
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ErrorScreen(
                    errorMessage = (errorState.errorMessage),
                    onRetry = { checkoutViewModel.loadCheckoutData() }
                )
            }
        }
        is CheckoutUiState.MissingAddress -> {
            AlertDialog(
                onDismissRequest = { showMissingAddressDialog.value = false },
                title = { Text(TITLE_SHIPPING_ADDRESS) },
                text = { Text(TEXT_MISSING_ADDRESS) },
                confirmButton = {
                    Button(onClick = {
                        navController.navigate(NavigationRoutes.PROFILE)
                        showMissingAddressDialog.value = false
                    }) {
                        Text(BUTTON_EDIT_PROFILE)
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CheckoutScreenPreview() {
    CheckoutScreen(
        navController = NavHostController(LocalContext.current),
        cartViewModel = hiltViewModel(),
        checkoutViewModel = hiltViewModel()
    )
}
