package com.mleon.mydeliveryapp.ui.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import android.util.Patterns
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import androidx.core.content.edit
import com.mleon.mydeliveryapp.data.repository.UserRepository
import kotlinx.coroutines.delay

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

    suspend fun onLoginClick() {
        val state = _uiState.value
        if (state.isFormValid) {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val result = userRepository.loginUser(
                    email = state.email,
                    password = state.password
                )
                Log.d("LoginViewModel", "Login result: $result")
                delay(1000)
                if (result != null) {
                    _uiState.update {
                        it.copy(
                            errorMessageLogin = null,
                            password = ""
                        )
                    }
                    sharedPreferences.edit { putString("email", _uiState.value.email) }
                } else {
                    _uiState.update {
                        it.copy(
                            errorMessageLogin = "Credenciales incorrectas",
                            isFormValid = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessageLogin = "Error: ${e.message}") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
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