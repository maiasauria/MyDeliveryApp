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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mleon.login.R
import com.mleon.utils.ui.LogoImage
import com.mleon.utils.ui.ValidatePasswordField
import com.mleon.utils.ui.ValidateTextField
import com.mleon.utils.ui.YappLogoLoadingIndicator

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
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.login_column_spacing)),
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
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.login_spacer_16)))
            if (errorMessageLogin.isNotEmpty()) {
                Text(
                    text = errorMessageLogin,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.login_error_bottom_padding))
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.login_spacer_16)))
            LoginButton(
                onClick = onLoginClick,
                isFormValid = isFormValid,
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.login_spacer_24)))
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
        YappLogoLoadingIndicator()
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
            .height(dimensionResource(id = R.dimen.login_button_height))
            .fillMaxWidth()
    ) {
        Text(stringResource(id = R.string.login_button))
    }
}

@Preview(showBackground = true)
@Composable
fun LoginViewPreview() {
    LoginView()
}