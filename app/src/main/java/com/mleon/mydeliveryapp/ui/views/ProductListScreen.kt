package com.mleon.mydeliveryapp.ui.views

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.mleon.mydeliveryapp.ui.viewmodel.ProductListViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.mleon.feature.cart.view.viewmodel.CartViewModel

@Composable
fun ProductListScreen(
    navController: NavHostController,
    productListViewModel: ProductListViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel()
) {
    val uiState by productListViewModel.productState.collectAsState()

    ProductListView(
        uiState = uiState,
        onSearchQueryChange = { productListViewModel.onSearchTextChange(it) },
        onCategorySelection = { productListViewModel.onCategorySelection(it) },
        onOrderByPriceDescending = { productListViewModel.onOrderByPriceDescending() },
        onOrderByPriceAscending = { productListViewModel.onOrderByPriceAscending() },
        onAddToCart = {
            cartViewModel.addToCart(it)
            productListViewModel.onAddToCartButtonClick(it)
        },
        onCartClick = { navController.navigate("cart") },
        clearCartMessage = { productListViewModel.clearCartMessage() },
        onProfileClick = { navController.navigate("profile") },
    )
}