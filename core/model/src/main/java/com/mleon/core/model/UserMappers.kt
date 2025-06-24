package com.mleon.core.model

fun UserDto.toUser(): User = User(
    email = this.email,
    name = this.name,
    lastname = this.lastname,
    address = this.address,
    userImageUrl = this.userImageUrl
)

fun User.toUserDto(password: String): UserDto = UserDto(
    email = this.email,
    name = this.name,
    lastname = this.lastname,
    address = this.address,
    userImageUrl = this.userImageUrl,
    password = password
)