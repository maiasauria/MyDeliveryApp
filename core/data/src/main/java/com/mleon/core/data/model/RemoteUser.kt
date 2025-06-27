package com.mleon.core.data.model

data class RemoteUser(
    val _id: String,
    val email: String,
    val name: String,
    val lastname: String,
    val address: String,
    val userImageUrl: String? = null,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)