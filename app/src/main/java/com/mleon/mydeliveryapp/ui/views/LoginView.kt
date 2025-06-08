package com.mleon.mydeliveryapp.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.runtime.Composable

@Composable
fun LoginView(
    navController: NavHostController,
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
    onSignupClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
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
            EmailField(
                value = email,
                onValueChange = onEmailChange,
                isEmailValid = isEmailValid,
                errorMessageEmail = errorMessageEmail
            )
            PasswordField(
                value = password,
                onValueChange = onPasswordChange,
                passwordVisible = passwordVisible,
                onVisibilityChange = onPasswordVisibilityChange,
                isPasswordValid = isPasswordValid,
                errorMessagePassword = errorMessagePassword
            )
            Spacer(modifier = Modifier.height(16.dp))
            LoginButton(
                onClick = onLoginClick,
                isFormValid = isFormValid
            )
            Spacer(modifier = Modifier.height(24.dp))
            TextButton(
                onClick = onSignupClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "¿No tenes una cuenta? Registrate acá")
            }
        }
    }
}
@Composable
fun LoginButton(
    onClick: () -> Unit,
    isFormValid: Boolean
) {
    Button(
        onClick = {
            onClick()
        },
        enabled = isFormValid,
        modifier = Modifier
            .height(48.dp)
            .fillMaxWidth()
    ) {
        Text("Iniciar Sesión", fontWeight = FontWeight.Bold)
    }
}

@Composable
fun LogoImage() {
    Image(
        painter = painterResource(id = com.mleon.utils.R.drawable.main_logo),
        contentDescription = "Logo"
    )
}


@Composable
fun EmailField(
    value: String,
    onValueChange: (String) -> Unit,
    isEmailValid: Boolean,
    errorMessageEmail: String?
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = { Text("Email") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        maxLines = 1,
        isError = !isEmailValid && value.isNotEmpty()
    )
    if (!isEmailValid && value.isNotEmpty()) {
        Text(
            text = errorMessageEmail ?: "",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
@Preview(showBackground = true)
fun EmailFieldPreview() {
    EmailField(
        value = "email@email.com",
        onValueChange = {},
        isEmailValid = true,
        errorMessageEmail = ""
    )
}

@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    passwordVisible: Boolean,
    onVisibilityChange: (Boolean) -> Unit,
    isPasswordValid: Boolean,
    errorMessagePassword: String?
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = { Text(text = "Password") },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        maxLines = 1,
        isError = !isPasswordValid && value.isNotEmpty(),
        trailingIcon = {
            val image =
                if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
            IconButton(onClick = { onVisibilityChange(!passwordVisible) }) {
                Icon(imageVector = image, contentDescription = null)
            }
        }
    )
    if (!isPasswordValid && value.isNotEmpty()) {
        Text(
            text = errorMessagePassword ?: "",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
}