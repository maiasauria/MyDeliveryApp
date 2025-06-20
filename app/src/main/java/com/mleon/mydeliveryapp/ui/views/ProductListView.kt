package com.mleon.mydeliveryapp.ui.views

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mleon.core.model.Categories
import com.mleon.core.model.Product
import com.mleon.mydeliveryapp.data.repository.ProductRepositoryImpl
import com.mleon.mydeliveryapp.viewmodel.ProductListState
import com.mleon.utils.ui.ListDivider
import com.mleon.utils.ui.ProductCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListView(
    uiState: ProductListState,
    onSearchQueryChange: (String) -> Unit,
    onCategorySelection: (Categories?) -> Unit,
    onOrderByPriceDescending: () -> Unit,
    onOrderByPriceAscending: () -> Unit,
    onAddToCart: (Product) -> Unit,
    clearCartMessage: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    var showSheet by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize())
        {
            SearchAndFiltersBar(
                uiState = uiState,
                onSearchQueryChange = onSearchQueryChange,
                onOpenFilters = {
                    showSheet = true
                },
            )
            FiltersRow(
                selectedCategory = uiState.selectedCategory,
                onCategorySelection = onCategorySelection
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item { if (uiState.isLoading) { LinearProgressIndicator(modifier = Modifier.fillMaxWidth()) } }

                items(uiState.products) { product ->
                    ProductCard(
                        product = product,
                        onAddToCart = { onAddToCart(product) },
                        isLoading = uiState.isLoading,
                    )
                    Spacer(modifier = Modifier.padding(vertical = 4.dp))
                    ListDivider()
                }
            }

            if (showSheet) {
                ProductsBottomSheet(
                    sheetState = sheetState,
                    showSheet = showSheet,
                    onDismissRequest = { showSheet = false },
                    onOrderByPriceDescending = onOrderByPriceDescending,
                    onOrderByPriceAscending = onOrderByPriceAscending
                )
            }
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



@Composable
fun FiltersRow(
    selectedCategory: Categories? = null,
    onCategorySelection: (Categories?) -> Unit,
){
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(Categories.entries) { category ->
            val isSelected = selectedCategory == category
            FilterChip(
                selected = isSelected,
                onClick = {
                    val newCategory =
                        if (selectedCategory == category) null else category
                    onCategorySelection(newCategory)
                },
                label = { Text(category.getCategoryName()) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    selectedContainerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray,
                ),
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsBottomSheet(
    sheetState: SheetState,
    showSheet: Boolean,
    onDismissRequest: () -> Unit,
    onOrderByPriceDescending: () -> Unit,
    onOrderByPriceAscending: () -> Unit
) {
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Ordenar por precio", style = MaterialTheme.typography.titleLarge)
                ListDivider()
                OutlinedButton(
                    onClick = {
                        onOrderByPriceAscending()
                        onDismissRequest()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Precio Ascendente")
                        Icon(Icons.Filled.ArrowUpward, contentDescription = "Orden Ascendente")
                    }
                }
                OutlinedButton(
                    onClick = {
                        onOrderByPriceDescending()
                        onDismissRequest()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Precio Descendente")
                        Icon(Icons.Filled.ArrowDownward, contentDescription = "Orden Descendente")
                    }
                }
            }
        }
    }
}

@Composable
fun SearchAndFiltersBar(
    uiState: ProductListState,
    onSearchQueryChange: (String) -> Unit,
    onOpenFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextField(
            value = uiState.searchQuery,
            onValueChange = onSearchQueryChange,
            label = { Text("Buscar") },
            placeholder = { Text("Buscar productos...") },
            modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                .weight(1f)
                ,
            trailingIcon = {
                if (uiState.searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Limpiar la búsqueda",
                        )
                    }
                }
            }
        )
        IconButton(onClick = onOpenFilters) {
            Icon(Icons.Filled.ImportExport,
                contentDescription = "Filtrar productos",
            modifier = Modifier
                )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    SearchAndFiltersBar(
        uiState = ProductListState(
            isLoading = false,
            products = emptyList(),
            searchQuery = "",
            selectedCategory = null,
            error = null,
            cartMessage = ""
        ),
        onSearchQueryChange = {},
        onOpenFilters = {},
    )
}


@Preview(showBackground = true)
@Composable
fun ProductListViewPreview() {
    ProductListView(
        uiState = ProductListState(
            isLoading = false,
            products = ProductRepositoryImpl().getProducts().take(10),
            searchQuery = "",
            selectedCategory = null,
            error = null,
            cartMessage = ""
        ),
        onSearchQueryChange = {},
        onCategorySelection = {},
        onOrderByPriceDescending = {},
        onOrderByPriceAscending = {},
        onAddToCart = {},
        clearCartMessage = {}
    )
}