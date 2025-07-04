package com.mleon.login.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mleon.login.R
import com.mleon.utils.ui.FullScreenLoadingIndicator
import com.mleon.utils.ui.LogoImage
import com.mleon.utils.ui.ValidatePasswordField
import com.mleon.utils.ui.ValidateTextField

@Composable
fun LoginView(
    email: String = "",
    onEmailChange: (String) -> Unit = {},
    isEmailValid: Boolean = true,
    errorMessageEmail: String = "",
    password: String = "",
    onPasswordChange: (String) -> Unit = {},
    passwordVisible: Boolean = false,
    onPasswordVisibilityChange: (Boolean) -> Unit = {},
    isPasswordValid: Boolean = true,
    errorMessagePassword: String = "",
    isFormValid: Boolean = false,
    onLoginClick: () -> Unit = {},
    onSignupClick: () -> Unit = {},
    errorMessageLogin: String = "",
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
                label = stringResource(id = R.string.login_email_label),
                isError = !isEmailValid,
                errorMessage = errorMessageEmail,
                enabled = !isLoading
            )
            ValidatePasswordField(
                value = password,
                onValueChange = onPasswordChange,
                label = stringResource(id = R.string.login_password_label),
                isError = !isPasswordValid,
                errorMessage = errorMessagePassword,
                passwordVisible = passwordVisible,
                onPasswordVisibilityChange = { onPasswordVisibilityChange(!passwordVisible) },
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (errorMessageLogin.isNotEmpty()) {
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
                Text(text = stringResource(id = R.string.login_no_account))
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
        Text(stringResource(id = R.string.login_button), fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun LoginViewPreview() {
    LoginView()
}