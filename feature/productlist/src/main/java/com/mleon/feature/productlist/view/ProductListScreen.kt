package com.mleon.feature.productlist.view

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
import com.mleon.feature.cart.view.viewmodel.CartViewModel
import com.mleon.feature.productlist.viewmodel.ProductListUiState
import com.mleon.feature.productlist.viewmodel.ProductListViewModel
import com.mleon.utils.ui.ErrorScreen
import com.mleon.utils.ui.YappLoadingIndicator

@Composable
fun ProductListScreen(
    onProductClick: (String) -> Unit,
    productListViewModel: ProductListViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel()
) {

    val uiState by productListViewModel.uiState.collectAsState()

    // Solo se ejecuta una vez al cargar la pantalla
    LaunchedEffect(Unit) {
        productListViewModel.loadProducts(refreshData = true)
    }

    when (uiState) {
        is ProductListUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                YappLoadingIndicator()
            }
        }

        is ProductListUiState.Success -> {
            val successState = uiState as ProductListUiState.Success
            val context = LocalContext.current
            val cartMessage = successState.cartMessage
            LaunchedEffect(cartMessage) {
                if (cartMessage.isNotEmpty()) {
                    Toast.makeText(context, cartMessage, Toast.LENGTH_SHORT).show()
                    productListViewModel.clearCartMessage()
                }
            }
            ProductListView(
                selectedCategory = successState.selectedCategory,
                searchQuery = successState.searchQuery,
                products = successState.products,
                onSearchQueryChange = { productListViewModel.onSearchTextChange(it) },
                onCategorySelection = { productListViewModel.onCategorySelection(it) },
                onOrderByPriceDescending = { productListViewModel.onOrderByPriceDescending() },
                onOrderByPriceAscending = { productListViewModel.onOrderByPriceAscending() },
                onAddToCart = {
                    cartViewModel.addToCart(it)
                    productListViewModel.onAddToCartButtonClick(it) },
                isAddingToCart = successState.isAddingToCart,
                onProductClick = { productId -> onProductClick(productId) }
            )
        }
        is ProductListUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ErrorScreen(
                    errorMessage = (uiState as ProductListUiState.Error).error,
                    onRetry = { productListViewModel.loadProducts(refreshData = true) }
                )
            }
        }
    }
}
