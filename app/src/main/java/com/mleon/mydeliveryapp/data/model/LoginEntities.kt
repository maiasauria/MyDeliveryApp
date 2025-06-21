package com.mleon.mydeliveryapp.data.model

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val message: String,
    val user: UserApiModel?
)

data class RegisterResponse(
    val email: String,
    val name: String,
    val password: String,
    val _id: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
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