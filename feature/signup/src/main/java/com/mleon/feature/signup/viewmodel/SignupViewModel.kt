package com.mleon.feature.signup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mleon.core.domain.usecase.user.RegisterUserUseCase
import com.mleon.core.domain.usecase.user.SaveUserEmailUseCase
import com.mleon.core.model.result.AuthResult
import com.mleon.utils.UserValidations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val ERROR_SIGNUP = "Por favor, completa todos los campos correctamente."
private const val ERROR_SIGNUP_PREFIX = "Error: "

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
    private val saveUserEmailUseCase: SaveUserEmailUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState: StateFlow<SignupUiState> = _uiState

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
        validateForm()
    }

    fun onLastnameChange(lastname: String) {
        _uiState.update { it.copy(lastname = lastname) }
        validateForm()
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
        validateForm()
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.update { it.copy(passwordConfirm = confirmPassword) }
        validateForm()
    }

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name) }
        validateForm()
    }

    fun onSignupClick() {
        validateForm()
        if (!_uiState.value.isFormValid) {
            _uiState.update { it.copy(errorMessageSignup = ERROR_SIGNUP) }
            return
        }
        signup()
    }

    private fun validateForm() {
        val state = _uiState.value

        val nameResult = UserValidations.validateName(state.name)
        val lastnameResult = UserValidations.validateLastname(state.lastname)
        val emailResult = UserValidations.validateEmail(state.email)
        val passwordResult = UserValidations.validatePassword(state.password)
        val passwordConfirmResult = UserValidations.validatePasswordConfirm(state.password, state.passwordConfirm)

        _uiState.update {
            it.copy(
                errorMessageName = if (state.name.isNotBlank() && !nameResult.isValid) nameResult.errorMessage ?: "" else "",
                errorMessageLastname = if (state.lastname.isNotBlank() && !lastnameResult.isValid) lastnameResult.errorMessage ?: "" else "",
                errorMessageEmail = if (state.email.isNotBlank() && !emailResult.isValid) emailResult.errorMessage ?: "" else "",
                errorMessagePassword = if (state.password.isNotBlank() && !passwordResult.isValid) passwordResult.errorMessage ?: "" else "",
                errorMessagePasswordConfirm = if (state.passwordConfirm.isNotBlank() && !passwordConfirmResult.isValid) passwordConfirmResult.errorMessage ?: "" else "",
                isFormValid = nameResult.isValid && lastnameResult.isValid &&
                        emailResult.isValid && passwordResult.isValid && passwordConfirmResult.isValid
            )
        }
    }


    private fun signup() {
        println("signup: ${_uiState.value}")
        _uiState.update { it.copy(isLoading = true, errorMessageSignup = "") }
        viewModelScope.launch(dispatcher) {
            try {
                val result = registerUserUseCase(name = _uiState.value.name,
                    lastname = _uiState.value.lastname,
                    email = _uiState.value.email,
                    password = _uiState.value.password)

                handleRegistrationResult(result)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessageSignup = ERROR_SIGNUP_PREFIX + (e.message ?: "")
                    )
                }
            }
        }
    }

    private fun handleRegistrationResult(result: AuthResult) {
        println("handleRegistrationResult: $result")
        when (result) {
            is AuthResult.Success -> {
                _uiState.update {
                    it.copy(
                        signupSuccess = true,
                        errorMessageSignup = "", // Limpiar errores previos
                        password = "",
                        passwordConfirm = "",
                        isLoading = false
                    )
                }

                saveUserEmailUseCase(result.user.email)
            }

            is AuthResult.Error -> {
                _uiState.update {
                    it.copy(
                        signupSuccess = false,
                        errorMessageSignup = result.errorMessage,
                        isLoading = false
                    )
                }
            }
        }
    }
}
