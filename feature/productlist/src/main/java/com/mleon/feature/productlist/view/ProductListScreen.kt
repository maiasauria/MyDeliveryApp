package com.mleon.feature.productlist.view

import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.mleon.feature.cart.view.viewmodel.CartViewModel
import com.mleon.feature.productlist.viewmodel.ProductListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    productListViewModel: ProductListViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel()
) {
    val uiState by productListViewModel.productState.collectAsState()

    val context = LocalContext.current

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    //Cart message
    val cartMessage = uiState.cartMessage ?: ""
    LaunchedEffect(cartMessage) {
        if (cartMessage.isNotEmpty()) {
            Toast.makeText(context, cartMessage, Toast.LENGTH_SHORT).show()
            productListViewModel.clearCartMessage()
        }
    }

// Error message
    val errorMessage = uiState.error?.message ?: ""
    LaunchedEffect(errorMessage) {
        if (errorMessage.isNotEmpty()) {
            Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_LONG).show()
        }
    }

    ProductListView(
        selectedCategory = uiState.selectedCategory,
        searchQuery = uiState.searchQuery,
        isLoading = uiState.isLoading,
        products = uiState.products,
        showBottomSheet = showBottomSheet,
        onShowBottomSheetChange = { showBottomSheet = it },
        sheetState = sheetState,
        onSearchQueryChange = { productListViewModel.onSearchTextChange(it) },
        onCategorySelection = { productListViewModel.onCategorySelection(it) },
        onOrderByPriceDescending = { productListViewModel.onOrderByPriceDescending() },
        onOrderByPriceAscending = { productListViewModel.onOrderByPriceAscending() },
        onAddToCart = {
            cartViewModel.addToCart(it)
            productListViewModel.onAddToCartButtonClick(it)
        }
    )
}