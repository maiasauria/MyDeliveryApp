package com.mleon.mydeliveryapp.view.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import com.mleon.mydeliveryapp.view.ui.views.ProductListView
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mleon.feature.cart.view.ui.fragments.CartViewModel
import com.mleon.mydeliveryapp.view.ui.viewmodel.ProductListViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Initialize ViewModels
            val viewModel: ProductListViewModel = viewModel()
            val cartViewModel: CartViewModel = viewModel()

            // Load products when the view is created
            val uiState = viewModel.uiState.collectAsState()

            when (val state = uiState.value) {
                is ProductListViewModel.ProductListUiState.Success -> {
                    ProductListView(
                        products = state.products,
                        onAddToCart = { product, quantity ->
                            repeat(quantity) { cartViewModel.addToCart(product) }
                        })
                }

                is ProductListViewModel.ProductListUiState.Loading -> {
                    Text("Loading products...")
                }

                is ProductListViewModel.ProductListUiState.Error -> {
                    Text("Error loading products: ${state.message}")
                }
            }
        }
    }
}