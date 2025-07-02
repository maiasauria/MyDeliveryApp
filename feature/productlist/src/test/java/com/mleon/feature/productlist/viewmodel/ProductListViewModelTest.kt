package com.mleon.feature.productlist.viewmodel

class ProductListViewModelTest {

// GIVEN getProductsUseCase returns empty list
// WHEN ProductListViewModel is initialized
// THEN productState contains empty products list

// GIVEN getProductsUseCase returns products
// WHEN ProductListViewModel is initialized
// THEN productState contains all products & products saved to room?

// GIVEN products loaded
// WHEN onSearchTextChange is called with matching query
// THEN productState contains filtered products

// GIVEN products loaded
// WHEN onSearchTextChange is called with empty query
// THEN productState contains all products

// GIVEN products loaded
// WHEN onCategorySelection is called with a category
// THEN productState contains products of that category

// GIVEN products loaded
// WHEN onCategorySelection is called with null
// THEN productState contains all products

// GIVEN filtered products
// WHEN onOrderByPriceAscending is called
// THEN productState contains products sorted by ascending price

// GIVEN filtered products
// WHEN onOrderByPriceDescending is called
// THEN productState contains products sorted by descending price

// GIVEN product selected
// WHEN onAddToCartButtonClick is called
// THEN productState contains cart message with product name

// GIVEN cart message is set
// WHEN clearCartMessage is called
// THEN productState cart message is empty

// GIVEN products filtered by category and search
// WHEN both filters are applied
// THEN productState contains products matching both filters

}