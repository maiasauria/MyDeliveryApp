package com.mleon.core.data.datasource.remote.dto

import com.google.gson.annotations.SerializedName
import com.mleon.core.model.User

data class UserDto(
    @SerializedName("_id")
    val id: String = "", // Lo genera el servidor
    val email: String,
    val name: String,
    val password: String,
    val lastname: String,
    val address: String ? = null,
    val userImageUrl: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
)

fun UserDto.toDomain() = User(
    email = email,
    name = name,
    lastname = lastname,
    address = address,
    userImageUrl = userImageUrl
)