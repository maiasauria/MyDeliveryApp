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
import com.mleon.feature.signup.viewmodel.SignupViewModel

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

    SignupView(
        navController = navController,
        name = uiState.name,
        onNameChange = { signupViewModel.onNameChange(it) },
        errorMessageName = uiState.errorMessageName,
        lastname = uiState.lastname,
        onLastnameChange = { signupViewModel.onLastnameChange(it) },
        errorMessageLastname = uiState.errorMessageLastname,
        email = uiState.email,
        onEmailChange = { signupViewModel.onEmailChange(it) },
        errorMessageEmail = uiState.errorMessageEmail,
        password = uiState.password,
        onPasswordChange = { signupViewModel.onPasswordChange(it) },
        errorMessagePassword = uiState.errorMessagePassword,
        passwordVisible = passwordVisible,
        onPasswordVisibilityChange = { passwordVisible = it },
        passwordConfirm = uiState.passwordConfirm,
        onPasswordConfirmChange = { signupViewModel.onConfirmPasswordChange(it) },
        errorMessagePasswordConfirm = uiState.errorMessagePasswordConfirm,
        confirmPasswordVisible = confirmPasswordVisible,
        onConfirmPasswordVisibilityChange = { confirmPasswordVisible = it },
        isFormValid = uiState.isFormValid,
        onSignupClick = { signupViewModel.onSignupButtonClick() },
        isLoading = uiState.isLoading,
        errorMessageSignup = uiState.errorMessageSignup,
    )
}
