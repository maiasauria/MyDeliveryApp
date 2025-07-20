package com.mleon.feature.productlist.view

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.mleon.feature.cart.view.viewmodel.CartViewModel
import com.mleon.feature.productlist.viewmodel.ProductListUiState
import com.mleon.feature.productlist.viewmodel.ProductListViewActions
import com.mleon.feature.productlist.viewmodel.ProductListViewModel
import com.mleon.feature.productlist.viewmodel.ProductListViewParams
import com.mleon.utils.ui.ErrorScreen
import com.mleon.utils.ui.YappFullScreenLoadingIndicator

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
            YappFullScreenLoadingIndicator()
        }

        is ProductListUiState.Success -> {
            val state = uiState as ProductListUiState.Success
            val context = LocalContext.current
            ShowCartToast(
                cartMessage = state.cartMessage,
                onClearMessage = { productListViewModel.clearCartMessage() },
                context = context
            )
            ProductListView(
                params = ProductListViewParams(
                    selectedCategory = state.selectedCategory,
                    searchQuery = state.searchQuery,
                    products = state.products,
                    isAddingToCart = state.isAddingToCart
                ),
                actions = ProductListViewActions(
                    onSearchQueryChange = { productListViewModel.onSearchTextChange(it) },
                    onCategorySelection = { productListViewModel.onCategorySelection(it) },
                    onOrderByPriceDescending = { productListViewModel.onOrderByPriceDescending() },
                    onOrderByPriceAscending = { productListViewModel.onOrderByPriceAscending() },
                    onAddToCart = {
                        cartViewModel.addToCart(it)
                        productListViewModel.onAddToCart(it)
                    },
                    onProductClick = { productId -> onProductClick(productId) },
                    onOrderByNameAscending = { productListViewModel.onOrderByNameAscending() },
                    onOrderByNameDescending = { productListViewModel.onOrderByNameDescending() },
                    onRefresh = { productListViewModel.loadProducts(refreshData = true) }
                )
            )
        }

        is ProductListUiState.Error -> {
            ErrorScreen(
                errorMessage = (uiState as ProductListUiState.Error).error,
                onRetry = { productListViewModel.loadProducts(refreshData = true) }
            )
        }
    }
}

@Composable
private fun ShowCartToast(
    cartMessage: String,
    onClearMessage: () -> Unit,
    context: android.content.Context
) {
    LaunchedEffect(cartMessage) {
        if (cartMessage.isNotEmpty()) {
            Toast.makeText(context, cartMessage, Toast.LENGTH_SHORT).show()
            onClearMessage()
        }
    }
}
