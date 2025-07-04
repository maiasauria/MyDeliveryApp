package com.mleon.core.model

import com.mleon.core.model.dtos.UserDto

data class User(
    val email: String,
    val name: String,
    val lastname: String,
    val address: String?,
    val userImageUrl: String? = null,
)

fun User.toUserDto(password: String): UserDto = UserDto(
    email = this.email,
    name = this.name,
    lastname = this.lastname,
    address = this.address,
    userImageUrl = this.userImageUrl,
    password = password
)