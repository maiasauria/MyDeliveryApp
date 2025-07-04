package com.mleon.feature.profile.viewmodel

data class ProfileUiState(
    val name: String = "",
    val isNameValid: Boolean = true,
    val errorMessageName: String = "",
    val lastname: String = "",
    val isLastnameValid: Boolean = true,
    val errorMessageLastname: String = "",
    val email: String = "",
    val isEmailValid: Boolean = true,
    val errorMessageEmail: String = "",
    val address: String = "",
    val isAddressValid: Boolean = true,
    val errorMessageAddress: String = "",
    val isFormValid: Boolean = false,
    val userImageUrl: String = "",
    val nacionalidad: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val isSaved: Boolean = false,
    val isImageUploading: Boolean = false,
)