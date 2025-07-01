package com.mleon.feature.signup.viewmodel

import android.content.SharedPreferences
import android.util.Log
import android.util.Patterns
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mleon.core.data.domain.RegisterUserParams
import com.mleon.core.data.domain.RegisterUserUseCase
import com.mleon.core.data.model.RegisterResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel
    @Inject
    constructor(
        private val sharedPreferences: SharedPreferences,
        private val registerUserUseCase: RegisterUserUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(SignupUiState())
    //_uiState.asStateFlow(): expone solo la interfaz inmutable (StateFlow), evitando que otras clases modifiquen el estado. TODO revisar el resto.
        val uiState: StateFlow<SignupUiState> = _uiState.asStateFlow()

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

        private val exceptionHandler = CoroutineExceptionHandler { _, exception -> println("Error occurred: ${exception.message}") }

        fun onSignupButtonClick() {
            validateForm()

            // Fail first: Si no es valido, no continuar con el registro
            if (!_uiState.value.isFormValid) {
                _uiState.update { it.copy(errorMessageSignup = "Por favor, completa todos los campos correctamente.") }
                return
            }

            _uiState.update { it.copy(isLoading = true, errorMessageSignup = "", signupSuccess = false) }

            //No indico el dispatcher porque el caso de uso ya lo maneja internamente
            viewModelScope.launch(exceptionHandler) {
                try {
                    val currentState = _uiState.value // Obtiene el estado actual una vez
                    val params = RegisterUserParams(
                        name = currentState.name,
                        lastname = currentState.lastname,
                        email = currentState.email,
                        password = currentState.password
                    )
                    val result = registerUserUseCase(params)

                    handleRegistrationResult(result)
                } catch (e: Exception) {
                    _uiState.update {
                        it.copy(
                            errorMessageSignup = "Error: ${e.message}",
                            isFormValid = false,
                        )
                    }
                } finally {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }

        private fun validateForm() {
            val currentState = _uiState.value // Obtiene el estado actual una vez
            val isNameValid = currentState.name.isNotBlank() && currentState.name.length in 2..20
            val isLastnameValid = currentState.lastname.isNotBlank() && currentState.lastname.length in 2..20
            val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(currentState.email).matches()
            val isPasswordValid = currentState.password.length in 8..12
            val isPasswordConfirmValid = currentState.password == currentState.passwordConfirm && currentState.passwordConfirm.isNotBlank()

            _uiState.update {
                it.copy(
                    errorMessageName = if (!isNameValid && currentState.name.isNotEmpty()) "El nombre no puede estar vacío" else "",
                    errorMessageLastname = if (!isLastnameValid && currentState.lastname.isNotEmpty()) { "El apellido no puede estar vacío" } else { "" },
                    errorMessageEmail = if (!isEmailValid && currentState.email.isNotEmpty()) "Email inválido" else "",
                    errorMessagePassword = if (!isPasswordValid && currentState.password.isNotEmpty()) { "La contraseña debe tener entre 8 y 12 caracteres" } else { "" },
                    errorMessagePasswordConfirm = if (!isPasswordConfirmValid && currentState.passwordConfirm.isNotEmpty()) { "Las contraseñas no coinciden" } else { "" },
                    isFormValid = isNameValid && isLastnameValid && isEmailValid && isPasswordValid && isPasswordConfirmValid,
                )
            }
        }

    private fun handleRegistrationResult(result: RegisterResult) {
        when (result) {
            is RegisterResult.Success -> {
                _uiState.update {
                    it.copy(
                        signupSuccess = true,
                        errorMessageSignup = "", // Limpiar errores previos
                        password = "",
                        passwordConfirm = ""
                    )

                }
                sharedPreferences.edit { putString("user_email", result.user.email) }
                Log.d("SignupViewModel", "User registered successfully: $result.user.email")
            }
            is RegisterResult.Error -> {
                _uiState.update {
                    it.copy(
                        signupSuccess = false,
                        errorMessageSignup = result.errorMessage
                    )
                }
                Log.w("SignupViewModel", "Registration failed: ${result.errorMessage}")
            }
        }
    }
    }
