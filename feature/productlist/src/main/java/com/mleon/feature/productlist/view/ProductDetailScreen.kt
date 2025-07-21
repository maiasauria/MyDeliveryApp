package com.mleon.feature.productlist.view

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mleon.feature.cart.viewmodel.CartViewModel
import com.mleon.feature.productlist.R
import com.mleon.feature.productlist.viewmodel.ProductDetailUiState
import com.mleon.feature.productlist.viewmodel.ProductDetailViewModel
import com.mleon.utils.ui.ErrorScreen
import com.mleon.utils.ui.YappFullScreenLoadingIndicator

@Composable
fun ProductDetailScreen(
    viewModel: ProductDetailViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel(),
    navController: NavController,
    productId: String,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(productId) {
        viewModel.loadProductDetail(productId)
    }

    when (uiState) {
        is ProductDetailUiState.Loading -> {
            YappFullScreenLoadingIndicator()
        }
        is ProductDetailUiState.Success -> {
            val successState = uiState as ProductDetailUiState.Success
            val context = LocalContext.current
            val cartMessage = successState.message

            LaunchedEffect(cartMessage) {
                if (cartMessage.isNotEmpty()) {
                    Toast.makeText(context, cartMessage, Toast.LENGTH_SHORT).show()
                    viewModel.clearMessage()
                }
            }

            ProductDetailView(
                product = successState.product,
                quantity = successState.quantityInCart,
                onQuantityChange = { viewModel.updateQuantity(it) },
                onAddToCart = { currentProduct, quantity ->
                    cartViewModel.addToCart(currentProduct, quantity)
                    viewModel.onAddToCart(currentProduct)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
        is ProductDetailUiState.Error -> {
            ErrorScreen(
                errorMessage = (uiState as ProductDetailUiState.Error).error.message ?:
                    stringResource(R.string.error_loading_product),
                onRetry = { viewModel.loadProductDetail(productId) }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ErrorProductDetailScreenPreview() {
    ErrorScreen(
        errorMessage = stringResource(R.string.error_loading_product),
        onRetry = {}
    )
}
