package com.mleon.feature.signup.viewmodel

import android.content.SharedPreferences
import android.util.Log
import android.util.Patterns
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mleon.core.data.model.RegisterResult
import com.mleon.core.data.repository.interfaces.UserRepository
import com.mleon.core.model.dtos.UserDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel
    @Inject
    constructor(
        private val userRepository: UserRepository,
        private val sharedPreferences: SharedPreferences,
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

        private val exceptionHandler =
            CoroutineExceptionHandler { _, exception ->
                println("Error occurred: ${exception.message}")
            }

        fun onSignupButtonClick() {
            validateForm()
            if (!_uiState.value.isFormValid) {
                _uiState.update { it.copy(errorMessageSignup = "Por favor, completa todos los campos correctamente.") }
                return
            }

            _uiState.update { it.copy(isLoading = true, errorMessageSignup = "") }

            viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
                try {
                    val userDto = createUserDto()
                    val result = userRepository.registerUser(userDto)
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
                    errorMessageLastname =
                        if (!isLastnameValid && currentState.lastname.isNotEmpty()) {
                            "El apellido no puede estar vacío"
                        } else {
                            ""
                        },
                    errorMessageEmail = if (!isEmailValid && currentState.email.isNotEmpty()) "Email inválido" else "",
                    errorMessagePassword =
                        if (!isPasswordValid && currentState.password.isNotEmpty()) {
                            "La contraseña debe tener entre 8 y 12 caracteres"
                        } else {
                            ""
                        },
                    errorMessagePasswordConfirm =
                        if (!isPasswordConfirmValid && currentState.passwordConfirm.isNotEmpty()) {
                            "Las contraseñas no coinciden"
                        } else {
                            ""
                        },
                    isFormValid = isNameValid && isLastnameValid && isEmailValid && isPasswordValid && isPasswordConfirmValid,
                )
            }
        }

        private fun createUserDto(): UserDto {
            // Accede al estado actual una vez para construir el DTO
            val currentState = _uiState.value
            return UserDto(
                name = currentState.name,
                email = currentState.email,
                password = currentState.password,
                lastname = currentState.lastname,
                address = "",
                userImageUrl = null,
            )
        }

        private fun handleRegistrationResult(result: RegisterResult) {
            if (result.user != null) {
                // Si el registro fue exitoso, actualiza el estado de la UI
                _uiState.update {
                    it.copy(
                        signupSuccess = true,
                        errorMessageSignup = "",
                        password = "",
                        passwordConfirm = "",
                    )
                }
                val user = result.user
                val userEmail = user?.email ?: ""

                // Guarda el email del usuario en SharedPreferences
                sharedPreferences.edit { putString("user_email", userEmail) }
                Log.d("SignupViewModel", "User registered successfully: $userEmail")
            } else {
                _uiState.update {
                    it.copy(
                        signupSuccess = false,
                        errorMessageSignup =
                            result.message
                                ?: "Error desconocido al registrar usuario.",
                        isFormValid = false,
                    )
                }
            }
        }
    }
