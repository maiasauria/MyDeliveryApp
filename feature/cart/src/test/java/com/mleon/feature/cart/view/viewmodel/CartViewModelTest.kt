package com.mleon.feature.cart.view.viewmodel

 class CartViewModelTest

// GIVEN cartRepository returns empty cart
 // WHEN CartViewModel is initialized
 // THEN uiState contains empty cartItems and total is zero

 // GIVEN cartRepository returns cart items
 // WHEN CartViewModel is initialized
 // THEN uiState contains cartItems and correct total

 // GIVEN cart has items
 // WHEN onRemoveItem is called
 // THEN uiState removes item and updates total

 // GIVEN cart has items
 // WHEN onIncreaseQuantity is called
 // THEN uiState increases item quantity and updates total

 // GIVEN cart has items
 // WHEN onDecreaseQuantity is called
 // THEN uiState decreases item quantity and updates total

 // GIVEN cart is not empty
 // WHEN onClearCart is called
 // THEN uiState contains empty cartItems and total is zero

 // GIVEN repository throws exception
 // WHEN CartViewModel is initialized
 // THEN uiState contains error message