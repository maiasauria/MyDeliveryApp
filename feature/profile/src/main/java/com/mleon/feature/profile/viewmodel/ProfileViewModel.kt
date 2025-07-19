package com.mleon.feature.profile.viewmodel

import android.app.Application
import android.net.Uri
import androidx.core.net.toUri
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val ERROR_UPDATE_IMAGE = "No se puede actualizar la imagen en este momento."
private const val ERROR_EDIT_PROFILE = "No se puede editar el perfil en este momento."
private const val ERROR_LOGOUT = "Error al cerrar sesión."
private const val ERROR_UPLOAD_IMAGE = "Error al subir la imagen."
private const val ERROR_UPDATE_PROFILE = "Error al actualizar el perfil."

private const val ERROR_NAME = "El nombre debe tener entre 2 y 50 caracteres"
private const val ERROR_LASTNAME = "El apellido debe tener entre 2 y 50 caracteres"
private const val ERROR_EMAIL = "El email no es válido"
private const val ERROR_ADDRESS = "La dirección no puede estar vacía"

private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")

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

    fun loadProfile() {
        viewModelScope.launch(dispatcher) {
            _uiState.value = ProfileUiState.Loading

            when (val result = getUserProfileUseCase()) {
                is AuthResult.Success -> {
                    val user = result.user
                    val formState = ProfileFormState()
                    val userDataState = UserDataState(
                        name = user.name,
                        lastname = user.lastname,
                        email = user.email,
                        address = user.address ?: "",
                        userImageUrl = user.userImageUrl ?: "",
                        userImageUri = (user.userImageUrl ?: "").toUri()
                    )
                    _uiState.value = ProfileUiState.Success(formState, userDataState)
                }

                is AuthResult.Error -> {
                    _uiState.value = ProfileUiState.Error(result.errorMessage)
                }
            }
        }
    }

    fun onImageUriChange(uri: Uri?) {
        val current = _uiState.value
        if (uri == null || current !is ProfileUiState.Success) {
            _uiState.value = ProfileUiState.Error(ERROR_UPDATE_IMAGE)
            return
        }
        _uiState.value = ProfileUiState.Success(
            current.data.copy(isImageChanged = true),
            current.userData.copy(userImageUri = uri, userImageUrl = uri.toString())
        )
    }

    fun clearSavedFlag() {
        val current = _uiState.value
        if (current is ProfileUiState.Success) {
            _uiState.value =
                ProfileUiState.Success(current.data.copy(isSaved = false), current.userData)
        }
    }

    fun onNameChange(newName: String) = updateFieldAndValidate(name = newName)
    fun onLastnameChange(newLastname: String) = updateFieldAndValidate(lastname = newLastname)
    fun onEmailChange(newEmail: String) = updateFieldAndValidate(email = newEmail)
    fun onAddressChange(newAddress: String) = updateFieldAndValidate(address = newAddress)

    private fun updateFieldAndValidate(
        name: String? = null,
        lastname: String? = null,
        email: String? = null,
        address: String? = null
    ) {
        val current = _uiState.value
        if (current !is ProfileUiState.Success) {
            _uiState.value = ProfileUiState.Error(ERROR_EDIT_PROFILE)
            return
        }

        val userData = current.userData
        _uiState.value = ProfileUiState.Success(
            current.data,
            userData.copy(
                name = name ?: userData.name,
                lastname = lastname ?: userData.lastname,
                email = email ?: userData.email,
                address = address ?: userData.address
            )
        )
        validateForm()
    }

    private fun validateForm() {
        val current = _uiState.value
        if (current !is ProfileUiState.Success) return

        val state = current.data
        val userData = current.userData
        val isNameValid = userData.name.trim().length in 2..50
        val isLastnameValid = userData.lastname.trim().length in 2..50
        val isEmailValid = emailRegex.matches(userData.email)
        val isAddressValid = userData.address.trim().isNotEmpty()

        _uiState.value = ProfileUiState.Success(
            state.copy(
                isFormValid = isNameValid && isLastnameValid && isEmailValid && isAddressValid,
            ),
            current.userData.copy(
                isNameValid = isNameValid,
                isLastnameValid = isLastnameValid,
                isEmailValid = isEmailValid,
                isAddressValid = isAddressValid,
                errorMessageName = if (!isNameValid) ERROR_NAME else "",
                errorMessageLastname = if (!isLastnameValid) ERROR_LASTNAME else "",
                errorMessageEmail = if (!isEmailValid) ERROR_EMAIL else "",
                errorMessageAddress = if (!isAddressValid) ERROR_ADDRESS else ""
            )
        )
    }

    fun updateProfile() {
        validateForm()

        viewModelScope.launch(dispatcher) {
            val currentState = _uiState.value as? ProfileUiState.Success ?: return@launch
            val userData = currentState.userData

            var newImageUrl = userData.userImageUrl

            _uiState.value = ProfileUiState.Success(
                currentState.data.copy(
                    isUploading = true,
                    isSaved = false
                ), currentState.userData
            )

            if (currentState.data.isImageChanged && userData.userImageUri != Uri.EMPTY) {
                try {
                    newImageUrl = uploadUserImageUseCase(userData.userImageUri)
                } catch (e: Exception) {
                    _uiState.value = ProfileUiState.Error(ERROR_UPLOAD_IMAGE)
                    return@launch
                }
            }

            val user = User(
                name = userData.name,
                lastname = userData.lastname,
                email = userData.email,
                address = userData.address,
                userImageUrl = newImageUrl
            )

            try {
                updateUserProfileUseCase(user)
                _uiState.value =
                    ProfileUiState.Success(
                        currentState.data.copy(isUploading = false, isSaved = true, isImageChanged = false),
                        currentState.userData.copy(userImageUrl = newImageUrl)
                    )
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(ERROR_UPDATE_PROFILE)
            }
        }
    }

    fun onLogoutClick() {
        viewModelScope.launch(dispatcher) {
            try {
                logoutUserUseCase()
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(ERROR_LOGOUT)
            }
        }
    }
}
