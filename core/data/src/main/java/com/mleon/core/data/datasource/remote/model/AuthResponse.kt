package com.mleon.core.data.datasource.remote.model

import com.mleon.core.data.datasource.remote.dto.UserDto
import com.mleon.core.model.User

data class AuthResponse(
    val message: String,
    val user: UserDto?
) {
    fun toUser(): User? {
        return user?.let {
            User(
                name = it.name,
                lastname = it.lastname,
                email = it.email,
                address = it.address ?: "",
                userImageUrl = it.userImageUrl
            )
        }
    }
}