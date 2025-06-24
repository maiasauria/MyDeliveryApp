package com.mleon.feature.profile.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mleon.core.data.repository.UserRepository
import com.mleon.core.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sharedPreferences: SharedPreferences,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val userEmail = sharedPreferences.getString("user_email", null)
                if (userEmail.isNullOrEmpty()) {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = "User not found")
                    }
                    return@launch //Termina la corrutina
                }

                val user = userRepository.getUserByEmail(userEmail)
                if (user != null) {
                    _uiState.update { state ->
                        state.copy(
                            name = user.name,
                            lastname = user.lastname,
                            email = user.email,
                            address = user.address ?: "",
                            userImageUrl = user.userImageUrl,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = "User not found")
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun updateProfile() {
        viewModelScope.launch {
            val state = _uiState.value
            try {
                _uiState.update {
                    it.copy(
                        isLoading = true,
                        errorMessage = null,
                        isSaved = false
                    )
                }
                val user = User(
                    name = state.name,
                    lastname = state.lastname,
                    email = state.email,
                    address = state.address,
                    userImageUrl = state.userImageUrl
                    // password removed
                )
                userRepository.updateUser(user)
                _uiState.update { it.copy(isLoading = false, isSaved = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun onNameChange(value: String) = _uiState.update { it.copy(name = value) }
    fun onLastnameChange(value: String) = _uiState.update { it.copy(lastname = value) }
    fun onEmailChange(value: String) = _uiState.update { it.copy(email = value) }
    fun onAddressChange(value: String) = _uiState.update { it.copy(address = value) }
    fun onUserImageUrlChange(value: String?) = _uiState.update { it.copy(userImageUrl = value) }
    fun clearSavedFlag() = _uiState.update { it.copy(isSaved = false) }

}