package com.mleon.core.data.datasource.remote.model

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

fun UserDto.toUser() = User(
    email = email,
    name = name,
    lastname = lastname,
    address = address,
    userImageUrl = userImageUrl
)

fun User.toUserDto(password: String): UserDto = UserDto(
    email = this.email,
    name = this.name,
    lastname = this.lastname,
    address = this.address ?: "",
    userImageUrl = this.userImageUrl,
    password = password,
    id = "", // Lo genera el servidor, se deja vacío
)