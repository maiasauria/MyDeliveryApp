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

data class LoginViewParams(
    val email: String = "",
    val isEmailValid: Boolean = true,
    val errorMessageEmail: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val isPasswordValid: Boolean = true,
    val errorMessagePassword: String = "",
    val isFormValid: Boolean = false,
    val errorMessageLogin: String = "",
    val isLoading: Boolean = false,
)

data class LoginViewActions(
    val onEmailChange: (String) -> Unit = {},
    val onPasswordChange: (String) -> Unit = {},
    val onPasswordVisibilityChange: (Boolean) -> Unit = {},
    val onLoginClick: () -> Unit = {},
    val onSignupClick: () -> Unit = {},
)

@Composable
private fun ErrorSection(params: LoginViewParams) {
    if (params.errorMessageLogin.isNotEmpty()) {
        Text(
            text = params.errorMessageLogin,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.login_error_bottom_padding))
        )
    }
}

@Composable
private fun ActionsSection(params: LoginViewParams, actions: LoginViewActions) {
    LoginButton(
        onClick = actions.onLoginClick,
        isFormValid = params.isFormValid,
        enabled = !params.isLoading
    )
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.login_spacer_24)))
    TextButton(
        onClick = actions.onSignupClick,
        modifier = Modifier.fillMaxWidth(),
        enabled = !params.isLoading
    ) {
        Text(text = stringResource(id = R.string.login_no_account))
    }
}

@Composable
fun LoginView(
    params: LoginViewParams,
    actions: LoginViewActions
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
                value = params.email,
                onValueChange = actions.onEmailChange,
                label = stringResource(id = R.string.login_email_label),
                isError = !params.isEmailValid,
                errorMessage = params.errorMessageEmail,
                enabled = !params.isLoading
            )

            ValidatePasswordField(
                value = params.password,
                onValueChange = actions.onPasswordChange,
                label = stringResource(id = R.string.login_password_label),
                isError = !params.isPasswordValid,
                errorMessage = params.errorMessagePassword,
                passwordVisible = params.passwordVisible,
                onPasswordVisibilityChange = { actions.onPasswordVisibilityChange(!params.passwordVisible) },
                enabled = !params.isLoading
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.login_spacer_16)))
            ErrorSection(params)
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.login_spacer_16)))
            ActionsSection(params, actions)
        }
    }
    if (params.isLoading) {
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
    LoginView(
        params = LoginViewParams(),
        actions = LoginViewActions()
    )
}
