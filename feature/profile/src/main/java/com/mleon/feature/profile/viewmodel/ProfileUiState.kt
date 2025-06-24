package com.mleon.feature.profile.viewmodel

data class ProfileUiState(
    val name: String = "",
    val lastname: String = "",
    val email: String = "",
    val address: String = "",
    val userImageUrl: String? = null,
    val nacionalidad: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSaved: Boolean = false
)