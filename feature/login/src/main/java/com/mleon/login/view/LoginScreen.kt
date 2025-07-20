package com.mleon.login.view

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
import com.mleon.login.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    navController: NavHostController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by loginViewModel.uiState.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }

    // Launch Effect (accion secundaria) cuando cambian los valores de loginSuccess o errorMessageLogin
    LaunchedEffect(uiState.loginSuccess, uiState.errorMessageLogin) {
        if (uiState.loginSuccess && uiState.errorMessageLogin.isEmpty()) {
            navController.navigate(NavigationRoutes.PRODUCTS) {
                popUpTo(NavigationRoutes.LOGIN) {
                    inclusive = true // elimina la pantalla de login del stack de navegación
                }
                launchSingleTop = true
            }
        }
    }

    val params = LoginViewParams(
        email = uiState.email,
        isEmailValid = uiState.isEmailValid,
        errorMessageEmail = uiState.errorMessageEmail,
        password = uiState.password,
        passwordVisible = passwordVisible,
        isPasswordValid = uiState.isPasswordValid,
        errorMessagePassword = uiState.errorMessagePassword,
        isFormValid = uiState.isFormValid,
        errorMessageLogin = uiState.errorMessageLogin,
        isLoading = uiState.isLoading,
    )
    val actions = LoginViewActions(
        onEmailChange = loginViewModel::onEmailChange,
        onPasswordChange = loginViewModel::onPasswordChange,
        onPasswordVisibilityChange = { passwordVisible = it },
        onLoginClick = loginViewModel::onLoginClick,
        onSignupClick = { navController.navigate(NavigationRoutes.SIGNUP) },
    )

    LoginView(
        params = params,
        actions = actions
    )
}
