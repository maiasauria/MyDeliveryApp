package com.mleon.feature.signup.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.mleon.core.navigation.NavigationRoutes
import com.mleon.feature.signup.R
import com.mleon.utils.ui.FullScreenLoadingIndicator
import com.mleon.utils.ui.LogoImage
import com.mleon.utils.ui.ValidateEmailField
import com.mleon.utils.ui.ValidatePasswordField
import com.mleon.utils.ui.ValidateTextField

@Composable
fun SignupView(
    navController: NavHostController,
    name: String = "",
    lastname: String = "",
    onNameChange: (String) -> Unit = {},
    onLastnameChange: (String) -> Unit = {},
    errorMessageName: String = "",
    errorMessageLastname: String = "",
    email: String = "",
    onEmailChange: (String) -> Unit = {},
    errorMessageEmail: String = "",
    password: String = "",
    onPasswordChange: (String) -> Unit = {},
    errorMessagePassword: String = "",
    passwordVisible: Boolean = false,
    onPasswordVisibilityChange: (Boolean) -> Unit = {},
    passwordConfirm: String = "",
    onPasswordConfirmChange: (String) -> Unit = {},
    onConfirmPasswordVisibilityChange: (Boolean) -> Unit = {},
    errorMessagePasswordConfirm: String = "",
    confirmPasswordVisible: Boolean = false,
    isFormValid: Boolean = false,
    onSignupClick: () -> Unit = {},
    errorMessageSignup: String = "",
    isLoading: Boolean = false,
) {
    Box(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.signup_screen_padding))
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.signup_field_spacing), Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LogoImage()
            ValidateTextField(
                value = name,
                onValueChange = onNameChange,
                label = stringResource(R.string.signup_name_label),
                isError = errorMessageName.isNotEmpty(),
                errorMessage = errorMessageName,
                enabled = !isLoading
            )
            ValidateTextField(
                value = lastname,
                onValueChange = onLastnameChange,
                label = stringResource(R.string.signup_lastname_label),
                isError = errorMessageLastname.isNotEmpty(),
                errorMessage = errorMessageLastname,
                enabled = !isLoading
            )
            ValidateEmailField(
                value = email,
                onValueChange = onEmailChange,
                label = stringResource(R.string.signup_email_label),
                isError = errorMessageEmail.isNotEmpty(),
                errorMessage = errorMessageEmail,
                enabled = !isLoading
            )
            ValidatePasswordField(
                value = password,
                onValueChange = onPasswordChange,
                label = stringResource(R.string.signup_password_label),
                isError = errorMessagePassword.isNotEmpty(),
                errorMessage = errorMessagePassword,
                passwordVisible = passwordVisible,
                onPasswordVisibilityChange = { onPasswordVisibilityChange(!passwordVisible) },
                enabled = !isLoading
            )
            ValidatePasswordField(
                value = passwordConfirm,
                onValueChange = onPasswordConfirmChange,
                label = stringResource(R.string.signup_confirm_password_label),
                isError = errorMessagePasswordConfirm.isNotEmpty(),
                errorMessage = errorMessagePasswordConfirm,
                passwordVisible = confirmPasswordVisible,
                onPasswordVisibilityChange = { onConfirmPasswordVisibilityChange(!confirmPasswordVisible) },
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.signup_button_top_spacing)))
            if (errorMessageSignup.isNotEmpty()) {
                Text(
                    text = errorMessageSignup,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.signup_error_bottom_padding))
                )
            }
            Button(
                onClick = {
                    onSignupClick()
                },
                enabled = isFormValid && !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.signup_button))
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.signup_text_button_top_spacing)))
            TextButton(
                onClick = {
                    navController.navigate(NavigationRoutes.LOGIN)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                Text(text = stringResource(R.string.signup_login_prompt))
            }
        }
    }
    if (isLoading) {
        FullScreenLoadingIndicator()
    }
}

@Preview(showBackground = true)
@Composable
fun SignupViewPreview() {
    SignupView(navController = NavHostController(LocalContext.current))
}