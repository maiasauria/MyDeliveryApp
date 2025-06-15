package com.mleon.mydeliveryapp.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.mleon.utils.ui.LoadingIndicator
import com.mleon.utils.ui.LogoImage
import com.mleon.utils.ui.ValidatePasswordField
import com.mleon.utils.ui.ValidateTextField

@Composable
fun LoginView(
    email: String,
    onEmailChange: (String) -> Unit,
    isEmailValid: Boolean,
    errorMessageEmail: String?,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    isPasswordValid: Boolean,
    errorMessagePassword: String?,
    isFormValid: Boolean,
    onLoginClick: () -> Unit,
    onSignupClick: () -> Unit,
    errorMessageLogin: String? = null,
    isLoading: Boolean
) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LogoImage()
            ValidateTextField(
                value = email,
                onValueChange = onEmailChange,
                label = "Email",
                isError = !isEmailValid,
                errorMessage = errorMessageEmail,
                enabled = !isLoading
            )
            ValidatePasswordField(
                value = password,
                onValueChange = onPasswordChange,
                label = "Contraseña",
                isError = !isPasswordValid,
                errorMessage = errorMessagePassword,
                passwordVisible = passwordVisible,
                onPasswordVisibilityChange = { onPasswordVisibilityChange(!passwordVisible) },
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (errorMessageLogin != null) {
                Text(
                    text = errorMessageLogin,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            LoginButton(
                onClick = onLoginClick,
                isFormValid = isFormValid,
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.height(24.dp))
            TextButton(
                onClick = onSignupClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                Text(text = "¿No tenes una cuenta? Registrate acá")
            }
        }
    }
    if (isLoading) {
        LoadingIndicator()
    }
}

@Composable
fun LoginButton(
    onClick: () -> Unit,
    isFormValid: Boolean,
    enabled: Boolean = true
) {
    Button(
        onClick = {
            onClick()
        },
        enabled = isFormValid && enabled,
        modifier = Modifier
            .height(48.dp)
            .fillMaxWidth()
    ) {
        Text("Iniciar Sesión", fontWeight = FontWeight.Bold)
    }
}

