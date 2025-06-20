package com.mleon.mydeliveryapp.viewmodel

data class SignupUiState(
    val email: String = "",
    val name: String = "",
    val password: String = "",
    val passwordConfirm: String = "",
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isNameValid: Boolean = false,
    val isFormValid: Boolean = false,
    val doPasswordsMatch: Boolean = false,
    val errorMessageEmail: String? = null,
    val errorMessagePassword: String? = null,
    val errorMessageName: String? = null,
    val errorMessageConfirmPassword: String? = null,
    val errorMessageSignup: String? = null,
    val isLoading: Boolean = false
)
