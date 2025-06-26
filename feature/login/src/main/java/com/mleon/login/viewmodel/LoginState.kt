package com.mleon.login.viewmodel

data class LoginUiState(
    //TODO borrar
    val email: String = "a@a.com",
    val password: String = "password456",
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isFormValid: Boolean = false,
    val errorMessageEmail: String = "",
    val errorMessagePassword: String = "",
    val errorMessageLogin: String = "",
    val loginSuccess: Boolean = false,
    val isLoading: Boolean = false
)
