package com.mleon.mydeliveryapp.view.viewmodel

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isFormValid: Boolean = false,
    val errorMessageEmail: String? = null,
    val errorMessagePassword: String? = null
)
