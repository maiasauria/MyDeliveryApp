package com.mleon.feature.profile.viewmodel

import android.net.Uri

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val data: ProfileFormState, val userData: UserDataState) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

data class UserDataState(
    val name: String = "",
    val lastname: String = "",
    val email: String = "",
    val address: String = "",
    val userImageUrl: String = "",
    val userImageUri: Uri,
    val isNameValid: Boolean = true,
    val isLastnameValid: Boolean = true,
    val isEmailValid: Boolean = true,
    val isAddressValid: Boolean = true,
    val errorMessageName: String = "",
    val errorMessageLastname: String = "",
    val errorMessageEmail: String = "",
    val errorMessageAddress: String = "",
)

data class ProfileFormState(
    val isFormValid: Boolean = false,
    val isImageChanged: Boolean = false,
    val isUploading: Boolean = false, //
    val isSaved: Boolean = false,
)
