package com.mleon.feature.orders.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.mleon.feature.orders.viewmodel.OrdersViewModel



@Composable
fun OrdersListScreen(
    viewModel: OrdersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchOrders()
    }

    OrdersListView(
        orders = uiState.orders,
        isLoading = uiState.isLoading,
        error = uiState.error
    )
}