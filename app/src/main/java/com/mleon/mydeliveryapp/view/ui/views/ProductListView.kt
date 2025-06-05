package com.mleon.mydeliveryapp.view.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mleon.core.model.Product
import com.mleon.mydeliveryapp.data.repository.ProductRepositoryImpl


@Composable
fun ProductListView(
    products: List<Product>,
    onAddToCart: (Product, Int) -> Unit,
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
        LazyColumn {
            item {
                Spacer(modifier = Modifier.height(16.dp)) // Espacio al final de la lista
            }
            item {
                Text(
                    text = "Total: \$${products.sumOf { it.price }}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
            items(products) { product ->
                ProductCard(
                    product = product,
                    showQuantitySelector = true,
                    onActionClick = onAddToCart,
                    actionButtonText = "Agregar al carrito"
                )
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProductListViewPreview() {
    val sampleProducts = ProductRepositoryImpl().getProducts().take(10)
    ProductListView(products = sampleProducts,
        onAddToCart = { product, quantity ->
            // Handle add to cart action
        },
        modifier = Modifier.fillMaxWidth()
    )
}