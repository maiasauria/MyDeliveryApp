package com.mleon.core.data.datasource.remote.model

import com.mleon.core.data.datasource.remote.dto.UserDto

// Resultado de Login
data class LoginResponse(
    val message: String?,
    val user: UserDto?
)