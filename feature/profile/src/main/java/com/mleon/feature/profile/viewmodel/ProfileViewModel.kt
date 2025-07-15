package com.mleon.feature.profile.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mleon.core.data.datasource.remote.model.AuthResult
import com.mleon.core.model.User
import com.mleon.feature.profile.usecase.GetUserProfileUseCase
import com.mleon.feature.profile.usecase.LogoutUserUseCase
import com.mleon.feature.profile.usecase.UpdateUserProfileUseCase
import com.mleon.feature.profile.usecase.UploadUserImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject
constructor(
    myApplication: Application,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val uploadUserImageUseCase: UploadUserImageUseCase,
    private val logoutUserUseCase: LogoutUserUseCase,
    private val dispatcher: CoroutineDispatcher
) : AndroidViewModel(myApplication) { // Extiende de AndroidViewModel para acceder al contexto de la aplicación

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val exceptionHandler =
        CoroutineExceptionHandler { _, exception ->
            Log.e("ProfileViewModel", "Coroutine error", exception)
            _uiState.value =
                ProfileUiState.Error("Ocurrió un error inesperado: ${exception.message}")
        }

    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")

    fun onImageUriChange(uri: Uri?) {
        if (uri != null) {
            uploadImage(uri)
        }
    }

    fun clearSavedFlag() {
        val current = _uiState.value
        if (current is ProfileUiState.Success) {
            _uiState.value = ProfileUiState.Success(current.data.copy(isSaved = false))
        }
    }

    fun onNameChange(newName: String) = updateFieldAndValidate(name = newName)
    fun onLastnameChange(newLastname: String) = updateFieldAndValidate(lastname = newLastname)
    fun onEmailChange(newEmail: String) = updateFieldAndValidate(email = newEmail)
    fun onAddressChange(newAddress: String) = updateFieldAndValidate(address = newAddress)

    fun onLogoutClick() {
        viewModelScope.launch(dispatcher + exceptionHandler) {
            try {
                logoutUserUseCase()
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error during logout", e)
                _uiState.value = ProfileUiState.Error("Error al cerrar sesión. Intenta nuevamente.")
            }
        }
    }

    fun loadProfile() {
        viewModelScope.launch(dispatcher + exceptionHandler) {
            _uiState.value = ProfileUiState.Loading

            when (val result = getUserProfileUseCase()) {
                is AuthResult.Success -> {
                    val user = result.user
                    val formState = ProfileFormState(
                        name = user.name,
                        lastname = user.lastname,
                        email = user.email,
                        address = user.address ?: "",
                        userImageUrl = user.userImageUrl ?: ""
                    )
                    _uiState.value = ProfileUiState.Success(formState)
                }

                is AuthResult.Error -> {
                    _uiState.value = ProfileUiState.Error(result.errorMessage)
                }
            }

        }
    }

    fun updateProfile() {
        validateForm()
        val current = _uiState.value
        if (current !is ProfileUiState.Success) return
        if (!current.data.isFormValid) {
            _uiState.value =
                ProfileUiState.Success(current.data.copy(errorMessage = "Por favor, completa todos los campos correctamente."))
            return
        }

        viewModelScope.launch(dispatcher + exceptionHandler) {
            val state = (_uiState.value as? ProfileUiState.Success)?.data ?: return@launch
            try {
                _uiState.value = ProfileUiState.Success(
                    state.copy(
                        isLoading = true,
                        errorMessage = "",
                        isSaved = false
                    )
                )
                val user = User(
                    name = state.name,
                    lastname = state.lastname,
                    email = state.email,
                    address = state.address,
                    userImageUrl = state.userImageUrl,
                )
                updateUserProfileUseCase(user)
                _uiState.value =
                    ProfileUiState.Success(state.copy(isLoading = false, isSaved = true))
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error("Error al actualizar el perfil: ${e.message}")
            }
        }
    }

    private fun uploadImage(uri: Uri) {
        val current = _uiState.value
        if (current !is ProfileUiState.Success) return
        viewModelScope.launch(dispatcher + exceptionHandler) {
            _uiState.value = ProfileUiState.Success(current.data.copy(isImageUploading = true))
            try {
                val imageUrl = uploadUserImageUseCase(uri)
                _uiState.value = ProfileUiState.Success(
                    (_uiState.value as ProfileUiState.Success).data.copy(
                        userImageUrl = imageUrl,
                        isImageUploading = false
                    )
                )
                validateForm()
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error("Error al subir la imagen.")
            }
        }
    }

    private fun updateFieldAndValidate(
        name: String? = null,
        lastname: String? = null,
        email: String? = null,
        address: String? = null
    ) {
        val current = _uiState.value
        if (current !is ProfileUiState.Success) {
            _uiState.value = ProfileUiState.Error("No se puede editar el perfil en este momento.")
            return
        }

        val state = current.data
        _uiState.value = ProfileUiState.Success(
            state.copy(
                name = name ?: state.name,
                lastname = lastname ?: state.lastname,
                email = email ?: state.email,
                address = address ?: state.address
            )
        )
        validateForm()
    }

    private fun validateForm() {
        val current = _uiState.value
        if (current !is ProfileUiState.Success) return
        val state = current.data
        val isNameValid = state.name.trim().length in 2..50
        val isLastnameValid = state.lastname.trim().length in 2..50
        val isEmailValid = emailRegex.matches(state.email)
        val isAddressValid = state.address.trim().isNotEmpty()
        _uiState.value = ProfileUiState.Success(
            state.copy(
                isNameValid = isNameValid,
                isLastnameValid = isLastnameValid,
                isEmailValid = isEmailValid,
                isAddressValid = isAddressValid,
                isFormValid = isNameValid && isLastnameValid && isEmailValid && isAddressValid,
                errorMessageName = if (!isNameValid) "El nombre debe tener entre 2 y 50 caracteres" else "",
                errorMessageLastname = if (!isLastnameValid) "El apellido debe tener entre 2 y 50 caracteres" else "",
                errorMessageEmail = if (!isEmailValid) "El email no es válido" else "",
                errorMessageAddress = if (!isAddressValid) "La dirección no puede estar vacía" else "",
            )
        )
    }
}
