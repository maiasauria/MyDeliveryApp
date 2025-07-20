package com.mleon.feature.signup.viewmodel

data class SignupUiState(
    val name: String = "",
    val errorMessageName: String = "",
    val lastname: String = "",
    val errorMessageLastname: String = "",
    val email: String = "",
    val errorMessageEmail: String = "",
    val password: String = "",
    val errorMessagePassword: String = "",
    val passwordConfirm: String = "",
    val errorMessagePasswordConfirm: String = "",
    val isFormValid: Boolean = false,
    val errorMessageSignup: String = "",
    val isLoading: Boolean = false,
    val signupSuccess: Boolean = false,
)

data class SignupViewParams(
    val name: String = "",
    val errorMessageName: String = "",
    val lastname: String = "",
    val errorMessageLastname: String = "",
    val email: String = "",
    val errorMessageEmail: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val errorMessagePassword: String = "",
    val confirmPassword: String = "",
    val confirmPasswordVisible: Boolean = false,
    val errorMessagePasswordConfirm: String = "",
    val isFormValid: Boolean = false,
    val errorMessageSignup: String = "",
    val isLoading: Boolean = false,
)

data class SignupViewActions(
    val onNameChange: (String) -> Unit = {},
    val onLastnameChange: (String) -> Unit = {},
    val onEmailChange: (String) -> Unit = {},
    val onPasswordChange: (String) -> Unit = {},
    val onConfirmPasswordChange: (String) -> Unit = {},
    val onPasswordVisibilityChange: (Boolean) -> Unit = {},
    val onConfirmPasswordVisibilityChange: (Boolean) -> Unit = {},
    val onSignupClick: () -> Unit = {},
    val onLoginClick: () -> Unit = {},
)