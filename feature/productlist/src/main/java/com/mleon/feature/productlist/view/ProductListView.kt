package com.mleon.feature.productlist.view

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.mleon.core.model.Product
import com.mleon.core.model.enums.Categories
import com.mleon.feature.productlist.R
import com.mleon.feature.productlist.viewmodel.ProductListViewActions
import com.mleon.feature.productlist.viewmodel.ProductListViewParams
import com.mleon.utils.ui.ListDivider
import com.mleon.utils.R as UtilsR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListView(
    params: ProductListViewParams,
    actions: ProductListViewActions
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val pullState = rememberPullToRefreshState()

    Column(modifier = Modifier.fillMaxSize()) {
        SearchAndFiltersBar(
            searchQuery = params.searchQuery,
            onSearchQueryChange = actions.onSearchQueryChange,
            onOpenFilters = { showBottomSheet = true },
        )
        FiltersRow(
            selectedCategory = params.selectedCategory,
            onCategorySelection = actions.onCategorySelection,
        )
        ProductListContent(
            products = params.products,
            isAddingToCart = params.isAddingToCart,
            pullState = pullState,
            onRefresh = actions.onRefresh,
            onAddToCart = actions.onAddToCart,
            onProductClick = actions.onProductClick
        )
        if (showBottomSheet) {
            ProductListBottomSheet(
                sheetState = sheetState,
                onDismiss = { showBottomSheet = false },
                actions = actions,
                selectedCategory = params.selectedCategory
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductListContent(
    products: List<Product>,
    isAddingToCart: Boolean,
    pullState: PullToRefreshState,
    onRefresh: () -> Unit,
    onAddToCart: (Product) -> Unit,
    onProductClick: (String) -> Unit
) {
    val isLoading = products.isEmpty() || isAddingToCart
    PullToRefreshBox(
        state = pullState,
        onRefresh = onRefresh,
        isRefreshing = isLoading,
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = isLoading,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                state = pullState
            )
        }
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(products) { index, product ->
                ProductCard(
                    product = product,
                    onAddToCart = { onAddToCart(product) },
                    isLoading = isAddingToCart,
                    onClick = { onProductClick(product.id) }
                )
                if (index < products.lastIndex) ListDivider()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    actions: ProductListViewActions,
    selectedCategory: Categories?
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = UtilsR.dimen.base_padding)),
        ) {
            Text(
                stringResource(R.string.productlist_sort_title),
                style = MaterialTheme.typography.titleLarge
            )
            CategoryChips(selectedCategory, actions.onCategorySelection)
            ListDivider()
            Text(
                stringResource(R.string.productlist_order_title),
                style = MaterialTheme.typography.titleLarge
            )
            OrderButtons(actions, onDismiss)
        }
    }
}

@Composable
private fun CategoryChips(
    selectedCategory: Categories?,
    onCategorySelection: (Categories?) -> Unit
) {
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
}

@Composable
private fun OrderButtons(
    actions: ProductListViewActions,
    onDismiss: () -> Unit
) {
    val buttons = listOf(
        Triple(
            stringResource(R.string.productlist_price_ascending),
            Icons.Filled.ArrowUpward,
            actions.onOrderByPriceAscending
        ),
        Triple(
            stringResource(R.string.productlist_price_descending),
            Icons.Filled.ArrowDownward,
            actions.onOrderByPriceDescending
        ),
        Triple(
            stringResource(R.string.productlist_name_ascending),
            Icons.Filled.ArrowUpward,
            actions.onOrderByNameAscending
        ),
        Triple(
            stringResource(R.string.productlist_name_descending),
            Icons.Filled.ArrowDownward,
            actions.onOrderByNameDescending
        )
    )
    buttons.forEach { (text, icon, callback) ->
        OrderButton(
            text = text,
            icon = icon,
            contentDesc = text,
            onClick = {
                callback()
                onDismiss()
            }
        )
    }
}

@Composable
fun FiltersRow(
    selectedCategory: Categories? = null,
    onCategorySelection: (Categories?) -> Unit,
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement
            .spacedBy(dimensionResource(id = UtilsR.dimen.filter_chip_spacing)),
    ) {
        items(Categories.entries.sortedBy { it.getCategoryName() }) { category ->
            val isSelected = selectedCategory == category
            FilterChip(
                selected = isSelected,
                onClick = {
                    val newCategory = if (isSelected) null else category
                    onCategorySelection(newCategory)
                },
                label = { Text(category.getCategoryName()) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedLabelColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else
                        MaterialTheme.colorScheme.onPrimary,
                    selectedContainerColor =
                        if (isSelected) MaterialTheme.colorScheme.secondaryContainer
                        else MaterialTheme.colorScheme.surfaceVariant,
                ),
          //      modifier = Modifier.padding(dimensionResource(id = UtilsR.dimen.filter_chip_padding)),
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
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(id = UtilsR.dimen.searchbar_vertical_padding)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            label = { Text(stringResource(R.string.productlist_search_label)) },
            placeholder = { Text(stringResource(R.string.productlist_search_placeholder)) },
            modifier = Modifier
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
            )
        }
    }
}
