package com.mleon.mydeliveryapp.view.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import android.util.Patterns
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import androidx.core.content.edit

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
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
                errorMessageEmail = if (!isEmailValid) "El email no es válido" else null
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
                errorMessagePassword = if (!isPasswordValid) "La contraseña debe tener entre 8 y 12 caracteres" else null
            )
        }
    }

    fun onLoginClick() {
        if (_uiState.value.isFormValid) {
            sharedPreferences.edit {
                putString("email", _uiState.value.email)
            }
        } else {
            _uiState.update { state ->
                state.copy(
                    errorMessageLogin = "Por favor, completa todos los campos correctamente."
                )
            }
        }
    }
}