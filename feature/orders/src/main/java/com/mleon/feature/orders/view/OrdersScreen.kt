package com.mleon.feature.orders.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.mleon.feature.orders.viewmodel.OrdersUiState
import com.mleon.feature.orders.viewmodel.OrdersViewModel
import com.mleon.utils.ui.ErrorScreen
import com.mleon.utils.ui.YappFullScreenLoadingIndicator

@Composable
fun OrdersScreen(
    viewModel: OrdersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadOrders()
    }

    when (uiState) {
        is OrdersUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                YappFullScreenLoadingIndicator()
            }
        }
        is OrdersUiState.Success -> {
            val orders = (uiState as OrdersUiState.Success).orders
            OrdersListView(orders = orders)
        }
        is OrdersUiState.Error -> {
            val errorMessage = (uiState as OrdersUiState.Error).error.message
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ErrorScreen(
                    errorMessage = errorMessage,
                    onRetry = { viewModel.loadOrders() }
                )
            }
        }
    }
}
