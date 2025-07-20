package com.mleon.feature.signup.view

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
import com.mleon.feature.signup.R
import com.mleon.feature.signup.viewmodel.SignupViewActions
import com.mleon.feature.signup.viewmodel.SignupViewParams
import com.mleon.utils.ui.LogoImage
import com.mleon.utils.ui.ValidatePasswordField
import com.mleon.utils.ui.ValidateTextField
import com.mleon.utils.ui.YappLogoLoadingIndicator

@Composable
fun SignupView(
    params: SignupViewParams,
    actions: SignupViewActions
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.signup_field_spacing)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LogoImage()
            ValidateTextField(
                value = params.name,
                onValueChange = actions.onNameChange,
                label = stringResource(id = R.string.signup_name_label),
                isError = params.errorMessageName.isNotEmpty(),
                errorMessage = params.errorMessageName,
                enabled = !params.isLoading
            )
            ValidateTextField(
                value = params.lastname,
                onValueChange = actions.onLastnameChange,
                label = stringResource(id = R.string.signup_lastname_label),
                isError = params.errorMessageLastname.isNotEmpty(),
                errorMessage = params.errorMessageLastname,
                enabled = !params.isLoading
            )
            ValidateTextField(
                value = params.email,
                onValueChange = actions.onEmailChange,
                label = stringResource(id = R.string.signup_email_label),
                isError = params.errorMessageEmail.isNotEmpty(),
                errorMessage = params.errorMessageEmail,
                enabled = !params.isLoading
            )
            ValidatePasswordField(
                value = params.password,
                onValueChange = actions.onPasswordChange,
                label = stringResource(id = R.string.signup_password_label),
                isError = params.errorMessagePassword.isNotEmpty(),
                errorMessage = params.errorMessagePassword,
                passwordVisible = params.passwordVisible,
                onPasswordVisibilityChange = {actions.onPasswordVisibilityChange(!params.passwordVisible) },
                enabled = !params.isLoading
            )
            ValidatePasswordField(
                value = params.confirmPassword,
                onValueChange = actions.onConfirmPasswordChange,
                label = stringResource(id = R.string.signup_confirm_password_label),
                isError = params.errorMessagePasswordConfirm.isNotEmpty(),
                errorMessage = params.errorMessagePasswordConfirm,
                passwordVisible = params.confirmPasswordVisible,
                onPasswordVisibilityChange = {actions.onConfirmPasswordVisibilityChange(!params.confirmPasswordVisible) },
                enabled = !params.isLoading
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.signup_spacer_16)))
            ErrorSection(params)
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.signup_spacer_16)))
            ActionsSection(params, actions)
        }
    }
    if (params.isLoading) {
        YappLogoLoadingIndicator()
    }
}

@Composable
private fun ErrorSection(params: SignupViewParams) {
    if (params.errorMessageSignup.isNotEmpty()) {
        Text(
            text = params.errorMessageSignup,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.signup_error_bottom_padding))
        )
    }
}


@Composable
private fun ActionsSection(params: SignupViewParams, actions: SignupViewActions) {
    Button(
        onClick = actions.onSignupClick,
        enabled = params.isFormValid && !params.isLoading,
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.signup_button_height))
            .fillMaxWidth()
    ) {
        Text(stringResource(id = R.string.signup_button))
    }
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.signup_spacer_24)))
    TextButton(
        onClick = actions.onLoginClick,
        modifier = Modifier.fillMaxWidth(),
        enabled = !params.isLoading
    ) {
        Text(text = stringResource(id = R.string.signup_login_prompt))
    }
}


@Preview(showBackground = true)
@Composable
fun SignupViewPreview() {
    SignupView(
        params = SignupViewParams(),
        actions = SignupViewActions()
    )
}