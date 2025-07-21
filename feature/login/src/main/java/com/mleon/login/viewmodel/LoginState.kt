package com.mleon.login.viewmodel

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isFormValid: Boolean = false,
    val errorMessageEmail: String = "",
    val errorMessagePassword: String = "",
    val errorMessageLogin: String = "",
    val loginSuccess: Boolean = false,
    val isLoading: Boolean = false
)
