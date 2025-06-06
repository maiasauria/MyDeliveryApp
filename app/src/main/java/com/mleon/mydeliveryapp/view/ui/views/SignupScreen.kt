package com.mleon.mydeliveryapp.view.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mleon.mydeliveryapp.view.viewmodel.SignupViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import com.mleon.utils.ui.ValidatePasswordField
import com.mleon.utils.ui.ValidateTextField

@Composable
fun SignupScreen(
    navController: NavHostController,
    signupViewModel: SignupViewModel = hiltViewModel()

) {
    val uiState by signupViewModel.uiState.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .fillMaxSize(),

        ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ValidateTextField(
                value = uiState.name,
                onValueChange = { signupViewModel.onNameChanged(it) },
                label = "Nombre Completo",
                isError = uiState.errorMessageName != null,
                errorMessage = uiState.errorMessageName,
                modifier = Modifier.fillMaxWidth()
            )
            ValidateTextField(
                value = uiState.email,
                onValueChange = { signupViewModel.onEmailChanged(it) },
                label = "Email",
                isError = uiState.errorMessageEmail != null,
                errorMessage = uiState.errorMessageEmail,
                modifier = Modifier.fillMaxWidth()
            )
            ValidatePasswordField(
                value = uiState.password,
                onValueChange = { signupViewModel.onPasswordChanged(it) },
                label = "Contraseña",
                isError = uiState.errorMessagePassword != null,
                errorMessage = uiState.errorMessagePassword,
                passwordVisible = passwordVisible,
                onPasswordVisibilityChange = { passwordVisible = !passwordVisible },
                modifier = Modifier.fillMaxWidth()
            )
            ValidatePasswordField(
                value = uiState.passwordConfirm,
                onValueChange = { signupViewModel.onConfirmPasswordChanged(it) },
                label = "Confirma tu contraseña",
                isError = uiState.errorMessageConfirmPassword != null,
                errorMessage = uiState.errorMessageConfirmPassword,
                passwordVisible = confirmPasswordVisible,
                onPasswordVisibilityChange = { confirmPasswordVisible = !confirmPasswordVisible },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    signupViewModel.registerUser()
                    navController.navigate("products") {
                        popUpTo("login") { inclusive = true } //
                    }
                },
                enabled = uiState.isFormValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarse")
            }
        }
    }
}
