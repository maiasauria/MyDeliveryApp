package com.mleon.feature.productlist.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mleon.core.model.Product
import com.mleon.core.model.enums.Categories
import com.mleon.feature.productlist.viewmodel.ProductListViewActions
import com.mleon.feature.productlist.viewmodel.ProductListViewParams


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
    ProductListBottomSheet(
        sheetState =
            rememberStandardBottomSheetState(
                initialValue = SheetValue.Expanded,
            ),
        selectedCategory = Categories.PIZZA,
        actions = ProductListViewActions(
            onSearchQueryChange = {},
            onCategorySelection = {},
            onOrderByPriceDescending = {},
            onOrderByPriceAscending = {},
            onAddToCart = {},
            onProductClick = {},
            onOrderByNameAscending = {},
            onOrderByNameDescending = {},
            onRefresh = {}
        ),
        onDismiss = { /* No hace nada en preview */ }
    )
}

@Preview(showBackground = true)
@Composable
fun ProductListViewPreview() {
    ProductListView(
        params = ProductListViewParams(
            selectedCategory = Categories.SALAD,
            searchQuery = "Pizza",
            products = List(10) {
                Product(
                    id = it.toString(),
                    name = "Product $it",
                    description = "Description $it",
                    price = it * 10.0,
                    imageUrl = "",
                    category = listOf(Categories.PIZZA, Categories.SALAD),
                    includesDrink = false,
                )
            },
            isAddingToCart = false
        ),
        actions = ProductListViewActions(
            onSearchQueryChange = {},
            onCategorySelection = {},
            onOrderByPriceDescending = {},
            onOrderByPriceAscending = {},
            onAddToCart = {},
            onProductClick = {},
            onOrderByNameAscending = {},
            onOrderByNameDescending = {},
            onRefresh = {}
        ))
}
