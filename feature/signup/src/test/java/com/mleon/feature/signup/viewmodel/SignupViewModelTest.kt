package com.mleon.feature.signup.viewmodel

import org.junit.Assert.*
 class SignupViewModelTest

// GIVEN userRepository returns success
// WHEN onSignupClick is called with valid data
// THEN uiState shows signup success and clears password

// GIVEN userRepository returns error
// WHEN onSignupClick is called with valid data
// THEN uiState shows error message

// GIVEN invalid email format
// WHEN onEmailChange is called
// THEN uiState shows email validation error

// GIVEN invalid password length
// WHEN onPasswordChange is called
// THEN uiState shows password validation error

// GIVEN valid form data
// WHEN onSignupClick is called
// THEN uiState sets isLoading true while waiting

// GIVEN signup is successful
// WHEN onSignupClick is called
// THEN user data is saved and success message is shown

// GIVEN signup fails with exception
// WHEN onSignupClick is called
// THEN uiState shows generic error message

// GIVEN form is invalid
// WHEN onSignupClick is called
// THEN uiState shows form validation error and does not call repository