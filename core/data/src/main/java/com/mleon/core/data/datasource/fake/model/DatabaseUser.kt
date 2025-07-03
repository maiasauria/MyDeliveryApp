package com.mleon.core.data.datasource.fake.model

import com.mleon.core.model.User

data class DatabaseUser(
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val lastname: String? = null,
    val address: String? = null,
    val userImageUrl: String? = null)

fun DatabaseUser.toUser(): User = User(
    name = this.name,
    email = this.email,
    lastname = this.lastname ?: "",
    address = this.address ?: "",
    userImageUrl = this.userImageUrl ?: ""
)