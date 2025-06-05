package com.mleon.mydeliveryapp.view.ui.views

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mleon.core.model.Product
import com.mleon.mydeliveryapp.data.repository.ProductRepositoryImpl
import com.mleon.mydeliveryapp.view.viewmodel.ProductListState


@Composable
fun ProductListView(
    state: ProductListState,
    innerPadding: PaddingValues,
    //onAddToCart: (Product, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color.White)
            .padding(16.dp)
    ) {
        TextField(
            value = "",
            onValueChange = {},
            label = { Text("Buscar") },
            placeholder = { Text("Buscar productos...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                if (state.isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp)) // Espacio al final de la lista
            }
            item {
                Text(
                    text = "Total: \$${state.products.sumOf { it.price }}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
            items(state.products) { product ->
                ProductCard(
                    product = product,
                    showQuantitySelector = false,
         //           onActionClick = Unit,
                    actionButtonText = "Agregar al carrito"
                )
            }
        }
        state.error?.let { error ->
            // Aquí puedes mostrar un mensaje de error, por ejemplo, con un SnackBar o un Toast
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