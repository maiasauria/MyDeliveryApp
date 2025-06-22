package com.mleon.mydeliveryapp.data.model

import com.mleon.core.model.UserDto

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val message: String,
    val user: UserApiModel?
)

data class LoginResult(
    val user: UserDto?,
    val message: String?
)

data class RegisterResult(
    val user: UserDto?,
    val message: String?
)

data class RegisterResponse(
    val _id: String? = null,
    val email: String? = null,
    val name: String? = null,
    val password: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val __v: Int? = null,
    val message: String? = null // present only on failure
)

data class UserApiModel(
    val _id: String,
    val email: String,
    val name: String,
    val password: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)