package com.mleon.core.data.model

import com.mleon.core.data.datasource.remote.model.RemoteUser
import com.mleon.core.model.User

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val message: String,
    val user: RemoteUser?
)

data class LoginResult(
    val user: User?,
    val message: String?
)

data class RegisterResult(
    val user: User?,
    val message: String?
)

data class RegisterResponse(
    val _id: String? = null,
    val email: String? = null,
    val name: String? = null,
    val lastname: String? = null,
    val address: String? = null,
    val userImageUrl: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val __v: Int? = null,
    val message: String? = null // presente solo si hay un error.
)
