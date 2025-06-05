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
    private val _uiState = MutableStateFlow(LoginUiState()

    )
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onEmailChanged(email: String) {
        _uiState.update { state ->
            val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
            state.copy(
                email = email,
                isEmailValid = isEmailValid,
                isFormValid = isEmailValid && state.isPasswordValid
            )
        }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { state ->
            val isPasswordValid = password.length in 8..12
            state.copy(
                password = password,
                isPasswordValid = isPasswordValid,
                isFormValid = isPasswordValid && state.isEmailValid
            )
        }
    }

    fun onLoginClicked() {
        // Handle login logic here, e.g., authenticate user
        if (_uiState.value.isFormValid) {
            sharedPreferences.edit {
                putString("email", _uiState.value.email)
            }
        } else {
            // Show error or feedback
        }
    }
}