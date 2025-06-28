package com.mleon.core.model.dtos

import com.mleon.core.model.User

data class UserDto(
    val email: String,
    val name: String,
    val lastname: String,
    val address: String?,
    val userImageUrl: String? = null,
    val password: String
)

fun UserDto.toUser(): User = User(
    email = this.email,
    name = this.name,
    lastname = this.lastname,
    address = this.address,
    userImageUrl = this.userImageUrl
)