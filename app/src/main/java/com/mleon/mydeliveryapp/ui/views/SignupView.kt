package com.mleon.mydeliveryapp.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mleon.mydeliveryapp.ui.viewmodel.SignupViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.mleon.utils.ui.ValidatePasswordField
import com.mleon.utils.ui.ValidateTextField

@Composable
fun SignupView(
    navController: NavHostController,
    name: String,
    onNameChange: (String) -> Unit,
    errorMessageName: String?,
    email: String,
    onEmailChange: (String) -> Unit,
    errorMessageEmail: String?,
    password: String,
    onPasswordChange: (String) -> Unit,
    errorMessagePassword: String?,
    passwordVisible: Boolean,
    onVisibilityChange: (Boolean) -> Unit,
    passwordConfirm: String,
    onPasswordConfirmChange: (String) -> Unit,
    onConfirmVisibilityChange: (Boolean) -> Unit,
    errorMessagePasswordConfirm: String?,
    confirmPasswordVisible: Boolean,
    isFormValid: Boolean,
    onSignupClick: () -> Unit,
    isLoading: Boolean //TODO implement
) {
    Scaffold(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = com.mleon.utils.R.drawable.main_logo), // Replace with your logo resource
                contentDescription = "Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            ValidateTextField(
                value = name,
                onValueChange = { onNameChange(it) },
                label = "Nombre Completo",
                isError = errorMessageName != null,
                errorMessage = errorMessageName,
                modifier = Modifier.fillMaxWidth()
            )
            ValidateTextField(
                value = email,
                onValueChange = { onEmailChange(it) },
                label = "Email",
                isError = errorMessageEmail != null,
                errorMessage = errorMessageEmail,
                modifier = Modifier.fillMaxWidth()
            )
            ValidatePasswordField(
                value = password,
                onValueChange = { onPasswordChange(it) },
                label = "Contraseña",
                isError = errorMessagePassword != null,
                errorMessage = errorMessagePassword,
                passwordVisible = passwordVisible,
                onPasswordVisibilityChange = { onVisibilityChange(!passwordVisible) },
                modifier = Modifier.fillMaxWidth()
            )
            ValidatePasswordField(
                value = passwordConfirm,
                onValueChange = { onPasswordConfirmChange(it) },
                label = "Confirma tu contraseña",
                isError = errorMessagePasswordConfirm != null,
                errorMessage = errorMessagePasswordConfirm,
                passwordVisible = confirmPasswordVisible,
                onPasswordVisibilityChange = { onConfirmVisibilityChange(!confirmPasswordVisible) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    onSignupClick()
                },
                enabled = isFormValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarse")
            }
            Spacer(modifier = Modifier.height(24.dp))
            TextButton(
                onClick = {
                    navController.navigate("login")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "¿Ya tenes una cuenta? Inicia sesion aca")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignupScreenPreview() {
    SignupScreen(navController = NavHostController(LocalContext.current))
}