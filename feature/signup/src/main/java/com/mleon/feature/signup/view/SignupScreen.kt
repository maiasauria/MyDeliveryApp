package com.mleon.feature.signup.view

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
import com.mleon.feature.signup.viewmodel.SignupViewActions
import com.mleon.feature.signup.viewmodel.SignupViewModel
import com.mleon.feature.signup.viewmodel.SignupViewParams

@Composable
fun SignupScreen(
    navController: NavHostController,
    signupViewModel: SignupViewModel = hiltViewModel()
) {
    val uiState by signupViewModel.uiState.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.signupSuccess, uiState.errorMessageSignup) {
        if (uiState.signupSuccess && uiState.errorMessageSignup.isEmpty()) {
            navController.navigate(NavigationRoutes.PRODUCTS) {
                popUpTo(NavigationRoutes.LOGIN) { inclusive = true }
            }
        }
    }

    val params = SignupViewParams(
        name = uiState.name,
        errorMessageName = uiState.errorMessageName,
        lastname = uiState.lastname,
        errorMessageLastname = uiState.errorMessageLastname,
        email = uiState.email,
        errorMessageEmail = uiState.errorMessageEmail,
        password = uiState.password,
        passwordVisible = passwordVisible,
        errorMessagePassword = uiState.errorMessagePassword,
        confirmPassword = uiState.passwordConfirm,
        confirmPasswordVisible = confirmPasswordVisible,
        errorMessagePasswordConfirm = uiState.errorMessagePasswordConfirm,
        isFormValid = uiState.isFormValid,
        errorMessageSignup = uiState.errorMessageSignup,
        isLoading = uiState.isLoading,
    )
    val actions = SignupViewActions(
        onNameChange = signupViewModel::onNameChange,
        onLastnameChange = signupViewModel::onLastnameChange,
        onEmailChange = signupViewModel::onEmailChange,
        onPasswordChange = signupViewModel::onPasswordChange,
        onConfirmPasswordChange = signupViewModel::onConfirmPasswordChange,
        onPasswordVisibilityChange = { passwordVisible = it },
        onConfirmPasswordVisibilityChange = { confirmPasswordVisible = it },
        onSignupClick = signupViewModel::onSignupClick,
        onLoginClick = { navController.navigate(NavigationRoutes.LOGIN) },
    )

    SignupView(
        params = params,
        actions = actions
    )
}
