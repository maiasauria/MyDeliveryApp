package com.mleon.login.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mleon.core.data.domain.LoginUserParams
import com.mleon.core.data.domain.LoginUserUseCase
import com.mleon.core.data.model.LoginResult
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
    private val loginUserUseCase: LoginUserUseCase,

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

    fun onLoginClick() {
        validateForm()

        if (!_uiState.value.isFormValid) {
            _uiState.update { it.copy(errorMessageLogin = "Por favor, completa todos los campos correctamente.") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessageLogin = "") }

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            try {
                val result = loginUserUseCase(LoginUserParams(email = _uiState.value.email, password = _uiState.value.password))
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

            //val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(currentState.email).matches()
            val isEmailValid = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$").matches(currentState.email)
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
        when (result) {
            is LoginResult.Success -> {
                _uiState.update {
                    it.copy(
                        loginSuccess = true,
                        errorMessageLogin = "", // Limpiar errores previos
                        password = "", // Limpiar contraseña del estado por seguridad
                    )
                }
                sharedPreferences.edit { putString("user_email", result.user.email) }
                Log.d("LoginViewModel", "Login successful for user: ${result.user.email}, Name: ${result.user.name}")
            }
            is LoginResult.Error -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        loginSuccess = false,
                        errorMessageLogin = result.errorMessage
                    )
                }
                Log.w("LoginViewModel", "Login failed: ${result.errorMessage} (Code: ${result.errorCode})")
            }
        }
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

}