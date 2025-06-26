package com.mleon.login.viewmodel

import android.content.SharedPreferences
import android.util.Log
import android.util.Patterns
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mleon.core.data.model.LoginResult
import com.mleon.core.data.repository.interfaces.UserRepository
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
        _uiState.update { it.copy(email = email) }
        validateForm()
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
        validateForm()
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.e("LoginViewModel", "Error en coroutine: ${exception.message}", exception)
        _uiState.update {
            it.copy(
                errorMessageLogin = "Ocurrió un error inesperado. Por favor, intenta de nuevo.",
                isLoading = false,
                isFormValid = false
            )
        }
    }

    /**
     * Handles the login button click event.
     * Validates the form and attempts to log in the user.
     * Updates the UI state based on the login result.
     */
    fun onLoginClick() {
        validateForm()

        if (!_uiState.value.isFormValid) {
            _uiState.update {
                it.copy(errorMessageLogin = "Por favor, completa todos los campos correctamente.")
            }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessageLogin = "") }

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            try {
                val result = userRepository.loginUser(
                    email = _uiState.value.email,
                    password = _uiState.value.password
                )
                handleLoginResult(result)

            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessageLogin = "Error: ${e.message}") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun validateForm() {
        _uiState.update { currentState ->
            val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(currentState.email).matches()
            val isPasswordValid = currentState.password.length in 8..12

            val isFormValid = isEmailValid && isPasswordValid

            currentState.copy(
                isEmailValid = isEmailValid,
                isPasswordValid = isPasswordValid,
                isFormValid = isFormValid,
                errorMessageEmail = if (!isEmailValid && currentState.email.isNotEmpty()) "El email no es válido" else "",
                errorMessagePassword = if (!isPasswordValid && currentState.password.isNotEmpty()) "La contraseña debe tener entre 8 y 12 caracteres" else ""
            )
        }
    }

    private fun handleLoginResult(result: LoginResult) {
        if (result.user != null) {
            val user = result.user
            val userEmail = user?.email ?: ""
            sharedPreferences.edit { putString("user_email", userEmail) }
            Log.d("LoginViewModel", "User logged in successfully: $userEmail")

            _uiState.update {
                it.copy(
                    loginSuccess = true,
                    errorMessageLogin = "",
                    password = ""
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    loginSuccess = false,
                    errorMessageLogin = result.message ?: "Credenciales incorrectas. Intenta de nuevo.",
                    isFormValid = false
                )
            }
        }
    }
}