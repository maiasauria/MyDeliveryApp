package com.mleon.feature.profile.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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
    private val myApplication: Application,
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
            _uiState.value = ProfileUiState.Error("Ocurrió un error inesperado. Intenta nuevamente.")
        }

    fun onImageUriChange(uri: Uri?) {
        if (uri != null) {
            uploadImage(uri)
            validateForm()
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
                _uiState.value = ProfileUiState.Error("Error during logout: ${e.message}")
            }
        }
    }

    fun loadProfile() {
        viewModelScope.launch(dispatcher + exceptionHandler) {
            _uiState.value = ProfileUiState.Loading
            try {
                val user = getUserProfileUseCase()
                if (user != null) {
                    val formState = ProfileFormState(
                        name = user.name,
                        lastname = user.lastname,
                        email = user.email,
                        address = user.address ?: "",
                        userImageUrl = user.userImageUrl ?: ""
                    )
                    _uiState.value = ProfileUiState.Success(formState)
                }
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e.message ?: "Error al cargar el perfil")
            }
        }
    }

    fun updateProfile() {
        validateForm()
        val current = _uiState.value
        if (current !is ProfileUiState.Success) return
        if (!current.data.isFormValid) {
            _uiState.value = ProfileUiState.Success(current.data.copy(errorMessage = "Por favor, completa todos los campos correctamente."))
            return
        }

        viewModelScope.launch(dispatcher + exceptionHandler) {
            val state = (_uiState.value as? ProfileUiState.Success)?.data ?: return@launch
            try {
                _uiState.value = ProfileUiState.Success(state.copy(isLoading = true, errorMessage = "", isSaved = false))
                val user = User(
                    name = state.name,
                    lastname = state.lastname,
                    email = state.email,
                    address = state.address,
                    userImageUrl = state.userImageUrl,
                )
                updateUserProfileUseCase(user)
                _uiState.value = ProfileUiState.Success(state.copy(isLoading = false, isSaved = true))
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error("Update failed: ${e.message}")
            }
        }
    }

    /* Uploads an image to Cloudinary and updates the user image URL in the UI state.
     * @param uri The URI of the image to upload.
     * This function runs in the IO dispatcher to avoid blocking the main thread.
     */
    private fun uploadImage(uri: Uri) {
        val current = _uiState.value
        if (current !is ProfileUiState.Success) return
        viewModelScope.launch(dispatcher + exceptionHandler) {
            _uiState.value = ProfileUiState.Success(current.data.copy(isImageUploading = true))
           // Log.d("ProfileViewModel", "Starting image upload for URI: $uri")
            try {
              val imageUrl = uploadUserImageUseCase(uri)
                _uiState.value = ProfileUiState.Success((_uiState.value as ProfileUiState.Success).data.copy(
                    userImageUrl = imageUrl,
                    isImageUploading = false
                ))
                validateForm()
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error("Error al subir la imagen.")
                Log.e("ProfileViewModel", "Error uploading image: ${e.message}")
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
        if (current !is ProfileUiState.Success) return
        val state = current.data
        val newName = name ?: state.name
        val newLastname = lastname ?: state.lastname
        val newEmail = email ?: state.email
        val newAddress = address ?: state.address

        val isNameValid = newName.trim().length in 6..50
        val isLastnameValid = newLastname.trim().length in 2..50
        val isEmailValid = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$").matches(state.email)
        val isAddressValid = newAddress.trim().isNotEmpty()

        _uiState.value = ProfileUiState.Success(
            state.copy(
                name = newName,
                lastname = newLastname,
                email = newEmail,
                address = newAddress,
                isNameValid = isNameValid,
                isLastnameValid = isLastnameValid,
                isEmailValid = isEmailValid,
                isAddressValid = isAddressValid,
                isFormValid = isNameValid && isLastnameValid && isEmailValid && isAddressValid,
                errorMessageName = if (!isNameValid) "El nombre debe tener entre 6 y 50 caracteres" else "",
                errorMessageLastname = if (!isLastnameValid) "El apellido debe tener entre 2 y 50 caracteres" else "",
                errorMessageEmail = if (!isEmailValid) "El email no es válido" else "",
                errorMessageAddress = if (!isAddressValid) "La dirección no puede estar vacía" else "",
            )
        )
    }

    private fun validateForm() {
        val current = _uiState.value
        if (current !is ProfileUiState.Success) return
        val state = current.data
        val isNameValid = state.name.trim().length in 6..50
        val isLastnameValid = state.lastname.trim().length in 2..50
        //val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(state.email).matches()
        val isEmailValid = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$").matches(state.email)
        val isAddressValid = state.address.trim().isNotEmpty()
        _uiState.value = ProfileUiState.Success(
            state.copy(
                isNameValid = isNameValid,
                isLastnameValid = isLastnameValid,
                isEmailValid = isEmailValid,
                isAddressValid = isAddressValid,
                isFormValid = isNameValid && isLastnameValid && isEmailValid && isAddressValid,
                errorMessageName = if (!isNameValid) "El nombre debe tener entre 6 y 50 caracteres" else "",
                errorMessageLastname = if (!isLastnameValid) "El apellido debe tener entre 2 y 50 caracteres" else "",
                errorMessageEmail = if (!isEmailValid) "El email no es válido" else "",
                errorMessageAddress = if (!isAddressValid) "La dirección no puede estar vacía" else "",
            )
        )
    }
}
