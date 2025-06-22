package com.mleon.mydeliveryapp.presentation.views

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mleon.core.navigation.NavigationRoutes
import com.mleon.mydeliveryapp.presentation.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    navController: NavHostController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by loginViewModel.uiState.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.loginSuccess, uiState.errorMessageLogin) {
        if (uiState.loginSuccess && uiState.errorMessageLogin == null) {
            navController.navigate(NavigationRoutes.PRODUCTS) {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    LoginView(
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
        onLoginClick = { loginViewModel.onLoginClick() },
        onSignupClick = { navController.navigate(NavigationRoutes.SIGNUP) },
        isLoading = uiState.isLoading,
        errorMessageLogin = uiState.errorMessageLogin,
    )
}

