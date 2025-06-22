package com.mleon.mydeliveryapp.presentation.views

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.mleon.feature.cart.view.viewmodel.CartViewModel
import com.mleon.mydeliveryapp.presentation.viewmodel.ProductListViewModel

@Composable
fun ProductListScreen(
    productListViewModel: ProductListViewModel = hiltViewModel(),
    cartViewModel: CartViewModel //no defino hiltViewModel() porque ya lo paso como parámetro desde el NavGraph
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
        clearCartMessage = { productListViewModel.clearCartMessage() }
    )
}