package com.mleon.mydeliveryapp.ui.views

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mleon.mydeliveryapp.ui.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    navController: NavHostController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by loginViewModel.uiState.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }

    LoginView(
        navController = navController,
        email = uiState.email,
        onEmailChange = { loginViewModel.onEmailChange(it) },
        isEmailValid = uiState.isEmailValid,
        errorMessageEmail = uiState.errorMessageEmail,
        password = uiState.password,
        onPasswordChange = { loginViewModel.onPasswordChange(it) },
        passwordVisible = passwordVisible,
        onPasswordVisibilityChange = { passwordVisible = it },
        isPasswordValid = uiState.isPasswordValid,
        errorMessagePassword = uiState.errorMessagePassword,
        isFormValid = uiState.isFormValid,
        onLoginClick = {
            loginViewModel.onLoginClick()
            navController.navigate("products") {
                popUpTo("login") { inclusive = true }
            }
        },
        onSignupClick = {
            navController.navigate("signup")
        }
    )
}