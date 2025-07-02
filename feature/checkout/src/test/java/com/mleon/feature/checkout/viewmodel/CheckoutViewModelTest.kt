package com.mleon.feature.checkout.viewmodel

class CheckoutViewModelTest {

// GIVEN cartItemRepository returns empty list
// WHEN CheckoutViewModel is initialized
// THEN uiState contains empty cartItems and validOrder is false
//Esto no deberia pasar.

// GIVEN cartItemRepository returns cart items
// WHEN CheckoutViewModel is initialized
// THEN uiState contains cartItems, validOrder is true, and amounts are calculated

// GIVEN valid cart items in uiState
// WHEN confirmOrder is called and orderRepository succeeds
// THEN uiState sets isLoading true, then orderConfirmed true and isLoading false

// GIVEN valid cart items in uiState
// WHEN confirmOrder is called and orderRepository throws exception
// THEN uiState sets errorMessage and isLoading false

// GIVEN any state
// WHEN onPaymentMethodSelection is called
// THEN uiState updates paymentMethod and validOrder is true

// GIVEN confirmOrder is called
// WHEN exception occurs in coroutine
// THEN uiState sets errorMessage to generic error message
}