package com.mleon.mydeliveryapp.ui.views

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mleon.mydeliveryapp.ui.viewmodel.SignupViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun SignupScreen(
    navController: NavHostController,
    signupViewModel: SignupViewModel = hiltViewModel()
) {
    val uiState by signupViewModel.uiState.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    SignupView(
        navController = navController,
        name = uiState.name,
        onNameChange = { signupViewModel.onNameChange(it) },
        errorMessageName = uiState.errorMessageName,
        email = uiState.email,
        onEmailChange = { signupViewModel.onEmailChange(it) },
        errorMessageEmail = uiState.errorMessageEmail,
        password = uiState.password,
        onPasswordChange = { signupViewModel.onPasswordChange(it) },
        errorMessagePassword = uiState.errorMessagePassword,
        passwordVisible = passwordVisible,
        onVisibilityChange = { passwordVisible = it },
        passwordConfirm = uiState.password,
        onPasswordConfirmChange = { signupViewModel.onPasswordChange(it) },
        errorMessagePasswordConfirm = uiState.errorMessagePassword,
        confirmPasswordVisible = confirmPasswordVisible,
        onConfirmVisibilityChange = { confirmPasswordVisible = it },
        isFormValid = uiState.isFormValid,
        onSignupClick = {
            coroutineScope.launch {
                signupViewModel.onLoginClick()
                navController.navigate("products") {
                    popUpTo("login") { inclusive = true }
                }
            }
        },
        isLoading = uiState.isLoading
    )
}