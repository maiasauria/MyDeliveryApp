package com.mleon.mydeliveryapp.view.ui.views

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mleon.core.model.Categories
import com.mleon.mydeliveryapp.data.repository.ProductRepositoryImpl
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.mleon.mydeliveryapp.R
import com.mleon.mydeliveryapp.view.viewmodel.ProductListState
import com.mleon.mydeliveryapp.view.viewmodel.ProductListViewModel
import com.mleon.utils.ui.ProductCard


@Composable
fun ProductListView(
    state: ProductListState,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
    productListViewModel: ProductListViewModel = hiltViewModel()
) {
    val searchQuery by remember { mutableStateOf(state.searchQuery) }
    var selectedCategory by remember { mutableStateOf<Categories?>(null) }
    val uiState by productListViewModel.productState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
        )
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon1),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(30.dp)
                        .padding(end = 8.dp)
                )
                TextField(
                    value = uiState.searchQuery,
                    onValueChange = { productListViewModel.onSearchTextChanged(it) },
                    label = { Text("Buscar") },
                    placeholder = { Text("Buscar productos...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 48.dp),
                    trailingIcon = {
                        if (uiState.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { productListViewModel.onSearchTextChanged("") }) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Limpiar la búsqueda",
                                )
                            }
                        }
                    },
                )
            }

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(Categories.entries) { category ->
                    val isSelected = selectedCategory == category
                    Button(
                        onClick = {
                            selectedCategory = if (selectedCategory == category) null else category
                            productListViewModel.onCategorySelected(selectedCategory)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray
                        )
                    ) {
                        Text(category.getCategoryName())
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
            ) {

                OutlinedButton(
                    onClick = { productListViewModel.orderByPriceDescending() },
                ) {
                    Text(
                        text = "Precio"
                    )
                    Icon(
                        imageVector = Icons.Filled.ArrowDownward,
                        contentDescription = "Precio descendente"
                    )
                }
                OutlinedButton(
                    onClick = { productListViewModel.orderByPriceAscending() },
                ) {
                    Text(
                        text = "Precio"
                    )
                    Icon(
                        imageVector = Icons.Filled.ArrowUpward,
                        contentDescription = "Precio ascendente"
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    if (state.isLoading) {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
//                item {
//                    Spacer(modifier = Modifier.height(24.dp)) // Espacio al final de la lista
//                }
//            item {  //TODO pasar esto al cart, no va aca
//                Text(
//                    text = "Total: \$${state.products.sumOf { it.price }}",
//                    style = MaterialTheme.typography.titleMedium,
//                    modifier = Modifier.padding(16.dp)
//                )
//            }
                items(state.products) { product ->
                    ProductCard(
                        product = product,
                        showQuantitySelector = false,
                        //           onActionClick = Unit,
                        actionButtonText = "Agregar al carrito"
                    )
                }
            }
        }
        FloatingActionButton(
            onClick = { },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .width(150.dp)
        ) {
            Text("Ver mi carrito")
        }
        state.error?.let { error ->
            Toast.makeText(
                LocalContext.current,
                "Error: ${error.message}",
                Toast.LENGTH_LONG
            ).show()
        }

    }
}


@Preview(showBackground = true)
@Composable
fun ProductListViewPreview() {
    val sampleProducts = ProductRepositoryImpl().getProducts().take(10)
    val sampleState = ProductListState(
        products = sampleProducts,
        isLoading = false,
        error = null
    )
    ProductListView(
        state = sampleState,
        innerPadding = PaddingValues(0.dp),
//        onAddToCart = { product, quantity ->
//            // Handle add to cart action
//        },
        modifier = Modifier.fillMaxWidth()
    )
}