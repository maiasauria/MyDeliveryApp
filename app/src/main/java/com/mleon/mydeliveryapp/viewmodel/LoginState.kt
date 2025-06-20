package com.mleon.mydeliveryapp.viewmodel

data class LoginUiState(
    val email: String = "a@a.com",
    val password: String = "password456",
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isFormValid: Boolean = false,
    val errorMessageEmail: String? = null,
    val errorMessagePassword: String? = null,
    val errorMessageLogin: String? = null,
    val isLoading: Boolean = false
)
