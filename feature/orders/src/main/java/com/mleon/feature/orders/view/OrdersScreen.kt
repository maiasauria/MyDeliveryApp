package com.mleon.feature.orders.view

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
import com.mleon.feature.orders.viewmodel.OrdersUiState
import com.mleon.feature.orders.viewmodel.OrdersViewModel
import com.mleon.utils.ui.ErrorScreen
import com.mleon.utils.ui.YappLoadingIndicator

@Composable
fun OrdersScreen(
    viewModel: OrdersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadOrders()
    }

    when (uiState) {
        is OrdersUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                YappLoadingIndicator()
            }
        }
        is OrdersUiState.Success -> {
            val orders = (uiState as OrdersUiState.Success).orders
            OrdersListView(orders = orders)
        }
        is OrdersUiState.Error -> {
            val errorMessage = (uiState as OrdersUiState.Error).error.message ?: "An unexpected error occurred. Please try again."
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ErrorScreen(
                    errorMessage = errorMessage,
                    onRetry = { viewModel.loadOrders() }
                )
            }
            LaunchedEffect(errorMessage) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }
}