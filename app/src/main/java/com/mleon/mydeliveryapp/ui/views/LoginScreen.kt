package com.mleon.mydeliveryapp.ui.views

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mleon.mydeliveryapp.ui.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavHostController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by loginViewModel.uiState.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

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
        onLoginClick = {
            coroutineScope.launch {
                loginViewModel.onLoginClick()
                if (loginViewModel.uiState.value.errorMessageLogin == null) {
                    navController.navigate("products") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
        },
        onSignupClick = {
            navController.navigate("signup")
        },
        isLoading = uiState.isLoading,
        errorMessageLogin = uiState.errorMessageLogin
    )
}