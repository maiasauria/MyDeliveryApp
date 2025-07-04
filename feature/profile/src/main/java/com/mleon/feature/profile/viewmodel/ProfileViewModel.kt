package com.mleon.feature.profile.viewmodel

import android.app.Application
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cloudinary.Cloudinary
import com.mleon.core.data.repository.interfaces.UserRepository
import com.mleon.core.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
    @Inject
    constructor(
        private val myApplication: Application,
        private val userRepository: UserRepository,
        private val sharedPreferences: SharedPreferences,
        private val cloudinary: Cloudinary,
    ) : AndroidViewModel(myApplication) {
        private val _uiState = MutableStateFlow(ProfileUiState())
        val uiState: StateFlow<ProfileUiState> = _uiState

        private val exceptionHandler =
            CoroutineExceptionHandler { _, exception ->
                Log.e("ProfileViewModel", "Coroutine error", exception)
                _uiState.update { it.copy(errorMessage = "Ocurrió un error inesperado. Intenta nuevamente.") }
            }

        init {
            loadProfile()
        }

        fun onImageUriChange(uri: Uri?) {
            if (uri != null) {
                viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
                    val url = uploadImage(uri)
                }
            }
        }

        fun clearSavedFlag() = _uiState.update { it.copy(isSaved = false) }

        fun onNameChange(name: String) {
            _uiState.update { it.copy(name = name) }
            validateForm()
        }

        fun onLastnameChange(lastname: String) {
            _uiState.update { it.copy(lastname = lastname) }
            validateForm()
        }

        fun onEmailChange(email: String) {
            _uiState.update { it.copy(email = email) }
            validateForm()
        }

        fun onAddressChange(address: String) {
            _uiState.update { it.copy(address = address) }
            validateForm()
        }

    private fun loadProfile() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            _uiState.update { it.copy(isLoading = true, errorMessage = "") }
            try {
                val userEmail = sharedPreferences.getString("user_email", null)
                if (userEmail.isNullOrEmpty()) {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = "User not found")
                    }
                    return@launch // Termina la corrutina
                }

                val user = userRepository.getUserByEmail(userEmail)
                if (user != null) {
                    _uiState.update { state ->
                        state.copy(
                            name = user.name,
                            lastname = user.lastname,
                            email = user.email,
                            address = user.address ?: "",
                            userImageUrl = user.userImageUrl ?: "",
                            isLoading = false,
                            errorMessage = "",
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = "User not found")
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "") }
            }
        }
    }

    fun updateProfile() {
        validateForm()
        if (!_uiState.value.isFormValid) {
            _uiState.update {
                it.copy(errorMessage = "Por favor, completa todos los campos correctamente.")
            }
            return
        }

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val state = _uiState.value
            try {
                _uiState.update {
                    it.copy(
                        isLoading = true,
                        errorMessage = "",
                        isSaved = false,
                    )
                }
                val user =
                    User(
                        name = state.name,
                        lastname = state.lastname,
                        email = state.email,
                        address = state.address,
                        userImageUrl = state.userImageUrl,
                    )
                userRepository.updateUser(user)
                _uiState.update { it.copy(isLoading = false, isSaved = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "") }
            }
        }
    }

    /* Uploads an image to Cloudinary and updates the user image URL in the UI state.
     * @param uri The URI of the image to upload.
     * This function runs in the IO dispatcher to avoid blocking the main thread.
     */
    private fun uploadImage(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            _uiState.update { it.copy(isImageUploading = true) }
            try {
                val inputStream =
                    getApplication<Application>()
                        .contentResolver
                        .openInputStream(uri)
                        ?: throw IllegalArgumentException("Unable to open input stream for URI: $uri")
                val result =
                    cloudinary
                        .uploader()
                        .upload(inputStream, mapOf("upload_preset" to "ProfileImage"))
                val imageUrl = result["secure_url"] as String
                _uiState.update { it.copy(userImageUrl = imageUrl) }
                updateProfile()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = e.message ?: "")
                }
                Log.e("ProfileViewModel", "Error uploading image: ${e.message}")
            } finally {
                _uiState.update { it.copy(isImageUploading = false) }
            }
        }
    }

    private fun validateForm() {
            _uiState.update { state ->
                val isNameValid = state.name.trim().length in 6..50
                val isLastnameValid = state.lastname.trim().length in 2..50
                val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(state.email).matches()
                val isAddressValid = state.address.trim().isNotEmpty()

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
            }
        }
    }
