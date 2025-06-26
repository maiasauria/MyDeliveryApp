package com.mleon.feature.signup.viewmodel

data class SignupUiState(
    val email: String = "",
    val name: String = "",
    val lastname: String = "",
    val password: String = "",
    val passwordConfirm: String = "",
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isNameValid: Boolean = false,
    val isLastnameValid: Boolean = false,
    val isFormValid: Boolean = false,
    val doPasswordsMatch: Boolean = false,
    val errorMessageEmail: String = "",
    val errorMessagePassword: String = "",
    val errorMessageName: String = "",
    val errorMessageLastname: String = "",
    val errorMessagePasswordConfirm: String = "",
    val errorMessageSignup: String = "",
    val signupSuccess: Boolean = false,
    val isLoading: Boolean = false
)
