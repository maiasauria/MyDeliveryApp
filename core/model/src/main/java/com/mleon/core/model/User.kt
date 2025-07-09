package com.mleon.core.model

data class User(
    val email: String,
    val name: String,
    val lastname: String,
    val address: String?,
    val userImageUrl: String? = null,
)

