package com.mleon.mydeliveryapp.presentation.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mleon.core.model.UserDto
import com.mleon.core.data.model.RegisterResult
import com.mleon.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val userRepository: com.mleon.core.data.repository.UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState: StateFlow<SignupUiState> = _uiState

    fun onEmailChange(email: String) {
        _uiState.update { state ->
            val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
            state.copy(
                email = email,
                isEmailValid = isEmailValid,
                isFormValid = isEmailValid && state.isPasswordValid && state.isNameValid && state.doPasswordsMatch,
                errorMessageEmail = if (!isEmailValid) "El email no es válido" else null,
                errorMessageSignup = null
            )
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { state ->
            val isPasswordValid = password.length in 8..12
            state.copy(
                password = password,
                isPasswordValid = isPasswordValid,
                isFormValid = isPasswordValid && state.isEmailValid && state.isNameValid && state.doPasswordsMatch,
                errorMessagePassword = if (!isPasswordValid) "La contraseña debe tener entre 8 y 12 caracteres" else null,
                errorMessageSignup = null
            )
        }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.update { state ->
            val doPasswordsMatch = confirmPassword == state.password
            state.copy(
                passwordConfirm = confirmPassword,
                doPasswordsMatch = doPasswordsMatch,
                isFormValid = state.isEmailValid && state.isPasswordValid && state.isNameValid && doPasswordsMatch,
                errorMessageConfirmPassword = if (!doPasswordsMatch) "Las contraseñas no coinciden" else null,
                errorMessageSignup = null
            )
        }
    }

    fun onNameChange(name: String) {
        _uiState.update { state ->
            val isNameValid = name.trim().length in 6..50
            state.copy(
                name = name,
                isNameValid = isNameValid,
                isFormValid = isNameValid && state.isEmailValid && state.isPasswordValid && state.doPasswordsMatch,
                errorMessageName = if (!isNameValid) "El nombre debe tener entre 6 y 50 caracteres" else null,
                errorMessageSignup = null
            )
        }
    }

    val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("Error occurred: ${exception.message}")
    }

    fun onSignupButtonClick() {
        if (_uiState.value.isFormValid) {
            _uiState.update { it.copy(isLoading = true, errorMessageSignup = null) }
            viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
                try {
                    val userDto = createUserDto()
                    val result = userRepository.registerUser(userDto)
                    Log.d("SignupViewModel", "Signup result: $result")
                    handleRegistrationResult(result)
                    //delay(1000)
                } catch (e: Exception) {
                    _uiState.update {
                        it.copy(
                            errorMessageSignup = "Error: ${e.message}",
                            isFormValid = false
                        )
                    }
                } finally {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        } else {
            _uiState.update {
                it.copy(
                    errorMessageSignup = "Por favor, completa todos los campos correctamente."
                )
            }

        }
    }

    private fun createUserDto(): UserDto {
        return UserDto(
            name = _uiState.value.name,
            email = _uiState.value.email,
            password = _uiState.value.password
        )
    }

    private fun handleRegistrationResult(result: com.mleon.core.data.model.RegisterResult) {
        if (result.user != null) {
            _uiState.update  {
                it.copy(
                    signupSuccess = true,
                    errorMessageSignup = null,
                    password = "",
                    passwordConfirm = ""
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    signupSuccess = false,
                    errorMessageSignup = result.message,
                    isFormValid = false
                )
            }
        }
    }
}
