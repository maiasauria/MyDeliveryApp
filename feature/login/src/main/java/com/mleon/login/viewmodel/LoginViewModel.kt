package com.mleon.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mleon.core.data.datasource.remote.model.AuthResult
import com.mleon.login.usecase.LoginUserParams
import com.mleon.login.usecase.LoginUserUseCase
import com.mleon.login.usecase.SaveUserEmailUseCase
import com.mleon.utils.UserValidations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val ERROR_LOGIN_PREFIX = "Error: "
private const val ERROR_LOGIN = "Error al iniciar sesión. Por favor, inténtalo de nuevo."

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    private val saveUserEmailUseCase: SaveUserEmailUseCase,
    private val dispatcher: CoroutineDispatcher

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
        validateForm() // Validar el formulario antes de intentar iniciar sesión

        if (!_uiState.value.isFormValid) {
            _uiState.update { it.copy(errorMessageLogin = ERROR_LOGIN) }
            return
        }

        login()
    }

    private fun validateForm() {
        val state = _uiState.value

        val emailResult = UserValidations.validateEmail(state.email)
        val passwordResult = UserValidations.validatePassword(state.password)
        val isFormValid = emailResult.isValid && passwordResult.isValid

        _uiState.update {
            it.copy(
                isEmailValid = emailResult.isValid,
                errorMessageEmail = emailResult.errorMessage ?: "",
                isPasswordValid = passwordResult.isValid,
                errorMessagePassword = passwordResult.errorMessage ?: "",
                isFormValid = isFormValid
            )
        }
    }

    private fun handleLoginResult(result: AuthResult) {
        when (result) {
            is AuthResult.Success -> {
                _uiState.update {
                    it.copy(
                        loginSuccess = true,
                        errorMessageLogin = "", // Limpiar errores previos
                        password = "", // Limpiar contraseña del estado por seguridad
                        isLoading = false,
                    )
                }
                saveUserEmailUseCase(result.user.email)
            }
            is AuthResult.Error -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        loginSuccess = false,
                        errorMessageLogin = result.errorMessage
                    )
                }
            }
        }
    }

    private fun login() {
        _uiState.update { it.copy(isLoading = true, errorMessageLogin = "") }
        viewModelScope.launch(dispatcher) {
            try {
                val result = loginUserUseCase(LoginUserParams(email = _uiState.value.email, password = _uiState.value.password))
                handleLoginResult(result)
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessageLogin = ERROR_LOGIN_PREFIX + (e.message ?: "")) }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

}
