package com.mleon.core.model

data class UserDto(
    val email: String,
    val name: String,
    val lastname: String,
    val address: String?,
    val userImageUrl: String? = null,
    val password: String
)