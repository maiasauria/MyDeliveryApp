package com.mleon.feature.productlist.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mleon.core.model.Product
import com.mleon.core.model.enums.Categories
import com.mleon.feature.productlist.R
import com.mleon.utils.ui.HorizontalLoadingIndicator
import com.mleon.utils.ui.ListDivider
import com.mleon.utils.ui.ProductCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListView(
    selectedCategory: Categories? = null,
    searchQuery: String = "",
    isLoading: Boolean = false,
    products: List<Product> = emptyList(),
    showBottomSheet: Boolean,
    onShowBottomSheetChange: (Boolean) -> Unit,
    sheetState: SheetState,
    onSearchQueryChange: (String) -> Unit,
    onCategorySelection: (Categories?) -> Unit,
    onOrderByPriceDescending: () -> Unit,
    onOrderByPriceAscending: () -> Unit,
    onAddToCart: (Product) -> Unit,
) {

    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SearchAndFiltersBar(
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                onOpenFilters = {
                    onShowBottomSheetChange(true)     },
            )
            FiltersRow(
                selectedCategory = selectedCategory,
                onCategorySelection = onCategorySelection,
            )

            LazyColumn(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(0.dp),
            ) {
                item {
                    if (isLoading) {
                        HorizontalLoadingIndicator()
                    }
                }

                itemsIndexed(products) { index, product ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(animationSpec = tween(2300, 50), initialAlpha = 0.3f),
                    ) {
                        ProductCard(
                            product = product,
                            onAddToCart = { onAddToCart(product) },
                            isLoading = isLoading,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        ListDivider()
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }

            if (showBottomSheet) {
                ProductsBottomSheet(
                    sheetState = sheetState,
                    showSheet = showBottomSheet,
                    onDismissRequest = { onShowBottomSheetChange(false)},
                    onOrderByPriceDescending = onOrderByPriceDescending,
                    onOrderByPriceAscending = onOrderByPriceAscending,
                )
            }
        }
    }
}

@Composable
fun FiltersRow(
    selectedCategory: Categories? = null,
    onCategorySelection: (Categories?) -> Unit,
) {
    LazyRow(
        modifier =
            Modifier
                .fillMaxWidth(),
        // .padding(bottom = 8.dp),
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
                colors =
                    FilterChipDefaults.filterChipColors(
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        selectedContainerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray,
                    ),
                modifier = Modifier.padding(4.dp),
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
    onOrderByPriceAscending: () -> Unit,
) {
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
            ) {
                Text(stringResource(R.string.productlist_order_by_price), style = MaterialTheme.typography.titleLarge)
                ListDivider()
                OutlinedButton(
                    onClick = {
                        onOrderByPriceAscending()
                        onDismissRequest()
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(stringResource(R.string.productlist_price_ascending))
                        Icon(Icons.Filled.ArrowUpward, contentDescription = stringResource(R.string.productlist_order_ascending_content_desc))

                    }
                }
                OutlinedButton(
                    onClick = {
                        onOrderByPriceDescending()
                        onDismissRequest()
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(stringResource(R.string.productlist_price_descending))
                        Icon(Icons.Filled.ArrowDownward, contentDescription = stringResource(R.string.productlist_order_descending_content_desc))

                    }
                }
            }
        }
    }
}

@Composable
fun SearchAndFiltersBar(
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit,
    onOpenFilters: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            label = { Text(stringResource(R.string.productlist_search_label)) },
            placeholder = { Text(stringResource(R.string.productlist_search_placeholder)) },
            modifier =
                Modifier
                    .defaultMinSize(minHeight = 48.dp)
                    .weight(1f),
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(R.string.productlist_clear_search_content_desc),

                        )
                    }
                }
            },
        )
        IconButton(onClick = onOpenFilters) {
            Icon(
                Icons.Filled.ImportExport,
                contentDescription = stringResource(R.string.productlist_filter_products_content_desc),
                modifier = Modifier,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchBarPreview() {
    SearchAndFiltersBar(
        searchQuery = "",
        onSearchQueryChange = {},
        onOpenFilters = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun FiltersRowPreview() {
    FiltersRow(
        selectedCategory = Categories.PIZZA,
        onCategorySelection = {},
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview()
@Composable
private fun ProductsBottomSheetPreview() {
    ProductsBottomSheet(
        sheetState =
            rememberStandardBottomSheetState(
                initialValue = SheetValue.Expanded,
            ),
        showSheet = true,
        onDismissRequest = {},
        onOrderByPriceDescending = {},
        onOrderByPriceAscending = {},
    )
}

// @Preview(showBackground = true)
// @Preview(showBackground = true, name = "ProductListView")
// @Composable
// fun ProductListViewPreview() {
//    ProductListView(
//        uiState = ProductListState(
//            isLoading = false,
//            products = ProductRepositoryImpl().getProducts().take(10),
//            searchQuery = "",
//            selectedCategory = null,
//            error = null,
//            cartMessage = ""
//        ),
//        onSearchQueryChange = {},
//        onCategorySelection = {},
//        onOrderByPriceDescending = {},
//        onOrderByPriceAscending = {},
//        onAddToCart = {},
//        clearCartMessage = {}
//    )
// }
