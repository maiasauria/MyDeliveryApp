package com.mleon.login.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mleon.utils.ui.FullScreenLoadingIndicator
import com.mleon.utils.ui.LogoImage
import com.mleon.utils.ui.ValidatePasswordField
import com.mleon.utils.ui.ValidateTextField

@Composable
fun LoginView(
    email: String = "",
    onEmailChange: (String) -> Unit = {},
    isEmailValid: Boolean = true,
    errorMessageEmail: String? = "",
    password: String = "",
    onPasswordChange: (String) -> Unit = {},
    passwordVisible: Boolean = false,
    onPasswordVisibilityChange: (Boolean) -> Unit = {},
    isPasswordValid: Boolean = true,
    errorMessagePassword: String? = "",
    isFormValid: Boolean = false,
    onLoginClick: () -> Unit = {},
    onSignupClick: () -> Unit = {},
    errorMessageLogin: String? = null,
    isLoading: Boolean = false,
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿No tenés una cuenta?",
                    style = MaterialTheme.typography.bodyMedium
                )
                TextButton(
                    onClick = onSignupClick,
                    enabled = !isLoading
                ) {
                    Text(text = "Registrate acá")
                }
            }
        }
    }
    if (isLoading) {
        FullScreenLoadingIndicator()
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

@Preview(showBackground = true)
@Composable
fun LoginViewPreview() {
    LoginView()
}
@Preview(showBackground = true)
@Composable
fun LoginViewPreviewError() {
    LoginView(
        isFormValid = false,
        errorMessageLogin = "Error al iniciar sesión. Por favor, intente nuevamente.",
    )
}