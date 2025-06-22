package com.mleon.mydeliveryapp.presentation.viewmodel

import android.content.SharedPreferences
import android.util.Patterns
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mleon.mydeliveryapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onEmailChange(email: String) {
        _uiState.update { state ->
            val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
            state.copy(
                email = email,
                isEmailValid = isEmailValid,
                isFormValid = isEmailValid && state.isPasswordValid,
                errorMessageEmail = if (!isEmailValid) "El email no es válido" else null,
                errorMessageLogin = null
            )
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { state ->
            val isPasswordValid = password.length in 8..12
            state.copy(
                password = password,
                isPasswordValid = isPasswordValid,
                isFormValid = isPasswordValid && state.isEmailValid,
                errorMessagePassword = if (!isPasswordValid) "La contraseña debe tener entre 8 y 12 caracteres" else null,
                errorMessageLogin = null
            )
        }
    }

    val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("Error occurred: ${exception.message}")
    }

    /**
     * Handles the login button click event.
     * Validates the form and attempts to log in the user.
     * Updates the UI state based on the login result.
     */
    fun onLoginClick() {

        if (_uiState.value.isFormValid) {
            _uiState.update { it.copy(isLoading = true) }

            viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
                try {
                    val result = userRepository.loginUser(
                        email = _uiState.value.email,
                        password = _uiState.value.password
                    )
                    if (result.user != null) {
                        _uiState.update {
                            it.copy(
                                loginSuccess = true,
                                errorMessageLogin = null,
                                password = "",
                            )
                        }
                        sharedPreferences.edit { putString("email", _uiState.value.email) }
                    } else {
                        _uiState.update {
                            it.copy(
                                loginSuccess = false,
                                errorMessageLogin = result.message,
                                isFormValid = false
                            )
                        }
                    }
                } catch (e: Exception) {
                    _uiState.update { it.copy(errorMessageLogin = "Error: ${e.message}") }
                } finally {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        } else {
            _uiState.update {
                it.copy(
                    errorMessageLogin = "Por favor, completa todos los campos correctamente."
                )
            }
        }
    }
}