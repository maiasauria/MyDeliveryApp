package com.mleon.core.data.datasource.remote.model

import com.mleon.core.model.User

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

fun RemoteUser.toUser() = User(
    email = email,
    name = name,
    lastname = lastname,
    address = address,
    userImageUrl = userImageUrl
)