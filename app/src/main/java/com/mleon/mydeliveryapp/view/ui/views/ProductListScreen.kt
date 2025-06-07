package com.mleon.mydeliveryapp.view.ui.views

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.mleon.mydeliveryapp.view.viewmodel.ProductListViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ProductListScreen(
    navController: NavHostController,
    productListViewModel: ProductListViewModel = hiltViewModel()
) {
    val uiState by productListViewModel.productState.collectAsState()

    ProductListView(
        uiState = uiState,
        onSearchQueryChange = { productListViewModel.onSearchTextChange(it) },
        onCategorySelection = { productListViewModel.onCategorySelection(it) },
        onOrderByPriceDescending = { productListViewModel.onOrderByPriceDescending() },
        onOrderByPriceAscending = { productListViewModel.onOrderByPriceAscending() },
        onAddToCart = { productListViewModel.onAddToCartButtonClick(it) },
        onCartClick = { navController.navigate("cart") },
        clearCartMessage = { productListViewModel.clearCartMessage() }
    )
}