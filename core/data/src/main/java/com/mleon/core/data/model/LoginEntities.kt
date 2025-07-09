package com.mleon.core.data.model

import com.mleon.core.data.datasource.remote.model.UserDto
import com.mleon.core.model.User

data class LoginRequest(
    val email: String,
    val password: String
)

sealed class AuthResult {
    data class Success(val message: String, val user: User) : AuthResult()
    data class Error(val errorMessage: String, val errorCode: Int? = null) : AuthResult()
}

// Respuestas directas de la API

data class AuthResponse(
    val message: String,
    val user: UserDto?
) {
    fun toUser(): User? {
        return user?.let {
            User(
                name = it.name,
                lastname = it.lastname,
                email = it.email,
                address = it.address ?: "",
                userImageUrl = it.userImageUrl
            )
        }
    }
}
