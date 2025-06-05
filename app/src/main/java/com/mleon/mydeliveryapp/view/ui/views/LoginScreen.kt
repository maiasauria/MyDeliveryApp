package com.mleon.mydeliveryapp.view.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.mleon.mydeliveryapp.view.viewmodel.LoginViewModel

@Composable
fun LoginScreen(navController: NavHostController, loginViewModel: LoginViewModel = viewModel()) {
    val uiState by loginViewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { loginViewModel.onEmailChanged(it) },
                label = { Text(text = "Email") },
                modifier = Modifier.fillMaxWidth(),
                isError = !uiState.isEmailValid && uiState.email.isNotEmpty()
            )
            if (!uiState.isEmailValid && uiState.email.isNotEmpty()) {
                Text(
                    text = "Invalid email format",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { loginViewModel.onPasswordChanged(it) },
                label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                isError = !uiState.isPasswordValid && uiState.password.isNotEmpty()
            )
            if (!uiState.isPasswordValid && uiState.password.isNotEmpty()) {
                Text(
                    text = "Password must be 8-12 characters",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Button(
                onClick = {
                    navController.navigate("products") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                enabled = uiState.isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Login")
            }
        }
    }
}