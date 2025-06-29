package com.mleon.feature.orders.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mleon.feature.orders.R
import com.mleon.feature.orders.viewmodel.OrdersViewModel
import com.mleon.utils.ui.HorizontalLoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersListScreen(
    viewModel: OrdersViewModel = hiltViewModel()
) {
    val orders by viewModel.orders.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchOrders()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.orders_title)) }) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when {
                isLoading -> {
                    HorizontalLoadingIndicator()
                }
                error != null -> {
                    Text(
                        text = stringResource(R.string.orders_error, error!!),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn {
                        items(orders) { order ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        stringResource(
                                            R.string.orders_quantity,
                                            order.productIds.sumOf { it.quantity }
                                        ),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        stringResource(
                                            R.string.orders_shipping_address,
                                            order.shippingAddress
                                        ),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        stringResource(
                                            R.string.orders_total,
                                            order.total
                                        )
                                    )
                                    Text(
                                        stringResource(R.string.orders_date, java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(java.util.Date(order.timestamp))
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    }