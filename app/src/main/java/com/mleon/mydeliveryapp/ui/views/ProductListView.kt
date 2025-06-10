package com.mleon.mydeliveryapp.ui.views

import android.widget.Toast
import androidx.compose.foundation.Image
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
import com.mleon.core.model.Categories
import com.mleon.mydeliveryapp.data.repository.ProductRepositoryImpl
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.mleon.core.model.Product
import com.mleon.mydeliveryapp.ui.viewmodel.ProductListState
import com.mleon.utils.ui.ProductCard


@Composable
fun ProductListView(
    uiState: ProductListState,
    onSearchQueryChange: (String) -> Unit,
    onCategorySelection: (Categories?) -> Unit,
    onOrderByPriceDescending: () -> Unit,
    onOrderByPriceAscending: () -> Unit,
    onAddToCart: (Product) -> Unit,
    onCartClick: () -> Unit,
    clearCartMessage: () -> Unit,
    modifier: Modifier = Modifier
) {

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
                    painter = painterResource(id = com.mleon.utils.R.drawable.icon1),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(30.dp)
                        .padding(end = 8.dp)
                )
                TextField(
                    value = uiState.searchQuery,
                    onValueChange = { onSearchQueryChange(it) },
                    label = { Text("Buscar") },
                    placeholder = { Text("Buscar productos...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 48.dp),
                    trailingIcon = {
                        if (uiState.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchQueryChange("") }) {
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
                    val isSelected = uiState.selectedCategory == category
                    Button(
                        onClick = {
                            val newCategory =
                                if (uiState.selectedCategory == category) null else category
                            onCategorySelection(newCategory)
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
                    onClick = { onOrderByPriceDescending() },
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
                    onClick = { onOrderByPriceAscending() },
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
                    if (uiState.isLoading) {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                items(uiState.products) { product ->
                    ProductCard(
                        product = product,
                        onAddToCart = {
                            onAddToCart(product)
                        },
                    )
                }
            }
        }
        FloatingActionButton(
            onClick = { onCartClick() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .width(150.dp)
        ) { 
            Text("Ver mi carrito")
        }
        uiState.error?.let { error ->
            Toast.makeText(
                LocalContext.current,
                "Error: ${error.message}",
                Toast.LENGTH_LONG
            ).show()
        }
        val context = LocalContext.current
        val cartMessage = uiState.cartMessage
        LaunchedEffect(cartMessage) {
            if (cartMessage.isNotEmpty()) {
                Toast.makeText(context, cartMessage, Toast.LENGTH_SHORT).show()
                clearCartMessage()
            }
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

}