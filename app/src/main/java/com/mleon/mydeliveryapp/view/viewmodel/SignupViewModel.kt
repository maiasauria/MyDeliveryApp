package com.mleon.mydeliveryapp.view.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.mleon.core.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(
        SignupUiState()
    )
    val uiState: StateFlow<SignupUiState> = _uiState
    val users = mutableListOf<User>()

    fun onEmailChanged(email: String) {
        _uiState.update { state ->
            val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
            state.copy(
                email = email,
                isEmailValid = isEmailValid,
                isFormValid = isEmailValid && state.isPasswordValid && state.isNameValid && state.doPasswordsMatch,
                errorMessageEmail = if (!isEmailValid) "El email no es válido" else null
            )
        }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { state ->
            val isPasswordValid = password.length in 8..12
            state.copy(
                password = password,
                isPasswordValid = isPasswordValid,
                isFormValid = isPasswordValid && state.isEmailValid && state.isNameValid && state.doPasswordsMatch,
                errorMessagePassword = if (!isPasswordValid) "La contraseña debe tener entre 8 y 12 caracteres" else null
            )
        }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _uiState.update { state ->
            val doPasswordsMatch = confirmPassword == state.password
            state.copy(
                passwordConfirm = confirmPassword,
                doPasswordsMatch = doPasswordsMatch,
                isFormValid = state.isEmailValid && state.isPasswordValid && doPasswordsMatch,
                errorMessageConfirmPassword = if (!doPasswordsMatch) "Las contraseñas no coinciden" else null
            )
        }
    }

    fun onNameChanged(name: String) {
        _uiState.update { state ->
            val isNameValid = name.trim().length in 6..50
            state.copy(
                name = name,
                isFormValid = isNameValid && state.isEmailValid && state.isPasswordValid && state.doPasswordsMatch,
                errorMessageName = if (!isNameValid) "El nombre debe tener entre 6 y 50 caracteres" else null,
            )
        }
    }


    fun registerUser() {
        val state = _uiState.value
        if (state.isFormValid) {
            users.add(User(state.name, state.email, state.password))
        }
    }
}