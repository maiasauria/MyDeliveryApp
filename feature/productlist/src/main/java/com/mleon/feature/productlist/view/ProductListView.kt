package com.mleon.feature.productlist.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mleon.core.model.Product
import com.mleon.core.model.enums.Categories
import com.mleon.feature.productlist.R
import com.mleon.utils.ui.ListDivider
import com.mleon.utils.R as UtilsR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListView(
    selectedCategory: Categories? = null,
    searchQuery: String = "",
    products: List<Product> = emptyList(),
    onSearchQueryChange: (String) -> Unit,
    onCategorySelection: (Categories?) -> Unit,
    onOrderByPriceDescending: () -> Unit = {},
    onOrderByPriceAscending: () -> Unit = {},
    onOrderByNameAscending: () -> Unit = {},
    onOrderByNameDescending: () -> Unit = {},
    onAddToCart: (Product) -> Unit = {},
    isAddingToCart: Boolean = false,
    onProductClick: (String) -> Unit = {},
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {},
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val pullState = rememberPullToRefreshState()

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SearchAndFiltersBar(
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                onOpenFilters = { showBottomSheet = true },
            )
            FiltersRow(
                selectedCategory = selectedCategory,
                onCategorySelection = onCategorySelection,
            )

            PullToRefreshBox (
                state = pullState,
                onRefresh = onRefresh,
                isRefreshing = isRefreshing,
                    indicator = {
                    Indicator(
                        modifier = Modifier.align(Alignment.TopCenter),
                        isRefreshing = isRefreshing,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        state = pullState
                    ) } )
             {
                LazyColumn(
                    modifier =
                        Modifier.fillMaxSize(),
                ) {
                    itemsIndexed(products) { index, product ->
                        ProductCard(
                            product = product,
                            onAddToCart = { onAddToCart(product) },
                            isLoading = isAddingToCart,
                            onClick = { onProductClick(it.id) }
                        )
                        if (index < products.lastIndex) {
                            ListDivider()
                        }
                    }
                }
            }
            if (showBottomSheet) {
                ProductsBottomSheet(
                    sheetState = sheetState,
                    onDismissRequest = { showBottomSheet = false },
                    onOrderByPriceDescending = onOrderByPriceDescending,
                    onOrderByPriceAscending = onOrderByPriceAscending,
                    onOrderByNameAscending = onOrderByNameAscending,
                    onOrderByNameDescending = onOrderByNameDescending,
                    selectedCategory = selectedCategory,
                    onCategorySelection = onCategorySelection,
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
            Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = UtilsR.dimen.filter_chip_spacing)),
    ) {
        items(Categories.entries.sortedBy { it.getCategoryName() }) { category ->
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
                        selectedContainerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,                    ),
                modifier = Modifier.padding(dimensionResource(id = UtilsR.dimen.filter_chip_padding)),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsBottomSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onOrderByPriceDescending: () -> Unit = {},
    onOrderByPriceAscending: () -> Unit = {},
    onOrderByNameAscending: () -> Unit = {},
    onOrderByNameDescending: () -> Unit = {},
    selectedCategory: Categories? = null,
    onCategorySelection: (Categories?) -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = UtilsR.dimen.base_padding)),
        ) {
            Text(
                stringResource(R.string.productlist_sort_title),
                style = MaterialTheme.typography.titleLarge
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = UtilsR.dimen.filter_chip_spacing)),
            ) {
                Categories.entries.sorted().forEach { category ->
                    val isSelected = selectedCategory == category
                    FilterChip(
                        selected = isSelected,
                        onClick = { onCategorySelection(category) },
                        label = { Text(category.getCategoryName()) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }
       //     Spacer(modifier = Modifier.height(dimensionResource(id = UtilsR.dimen.sheet_content_spacing)))
            ListDivider()
            Text(
                stringResource(R.string.productlist_order_title),
                style = MaterialTheme.typography.titleLarge
            )
            OrderButton(
                text = stringResource(R.string.productlist_price_ascending),
                icon = Icons.Filled.ArrowUpward,
                contentDesc = stringResource(R.string.productlist_order_ascending_content_desc),
                onClick = {
                    onOrderByPriceAscending()
                    onDismissRequest()
                },
            )
            OrderButton(
                text = stringResource(R.string.productlist_price_descending),
                icon = Icons.Filled.ArrowDownward,
                contentDesc = stringResource(R.string.productlist_order_descending_content_desc),
                onClick = {
                    onOrderByPriceDescending()
                    onDismissRequest()
                },
            )
            OrderButton(
                text = stringResource(R.string.productlist_name_ascending),
                icon = Icons.Filled.ArrowUpward,
                contentDesc = stringResource(R.string.productlist_name_ascending_content_desc),
                onClick = {
                    onOrderByNameAscending()
                    onDismissRequest()
                },
            )
            OrderButton(
                text = stringResource(R.string.productlist_name_descending),
                icon = Icons.Filled.ArrowDownward,
                contentDesc = stringResource(R.string.productlist_name_descending_content_desc),
                onClick = {
                    onOrderByNameDescending()
                    onDismissRequest()
                },
            )
        }
    }
}

@Composable
private fun OrderButton(
    text: String,
    icon: ImageVector,
    contentDesc: String,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text)
            Icon(icon, contentDescription = contentDesc)
        }
    }
}

@Composable
fun SearchAndFiltersBar(
    searchQuery: String = "",
    modifier: Modifier = Modifier,
    onSearchQueryChange: (String) -> Unit,
    onOpenFilters: () -> Unit,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(id = UtilsR.dimen.searchbar_vertical_padding)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            label = { Text(stringResource(R.string.productlist_search_label)) },
            placeholder = { Text(stringResource(R.string.productlist_search_placeholder)) },
            modifier =
                Modifier
                    .defaultMinSize(minHeight = dimensionResource(id = UtilsR.dimen.textfield_min_height))
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
        onDismissRequest = {},
        selectedCategory = Categories.PIZZA,
        onCategorySelection = {},
    )
}

@Preview(showBackground = true)
@Preview(showBackground = true, name = "ProductListView")
@Composable
fun ProductListViewPreview() {
    ProductListView(
        selectedCategory = Categories.PIZZA,
        searchQuery = "Pizza",
        products = List(10) {
            Product(
                id = it.toString(),
                name = "Product $it",
                description = "Description $it",
                price = it * 10.0,
                imageUrl = "",
                category = listOf(Categories.PIZZA),
                includesDrink = false,
            )
        }, onSearchQueryChange = {},
        onCategorySelection = {},
        onOrderByPriceDescending = {},
        onOrderByPriceAscending = {},
        onAddToCart = {},
        isAddingToCart = false,
        onProductClick = {}
    )
}
