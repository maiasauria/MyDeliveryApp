package com.mleon.core.model

data class DatabaseUser(
    val id: Int,
    val name: String,
    val email: String,
    val password: String, val lastname: String? = null, val address: String? = null, val userImageUrl: String? = null)

fun DatabaseUser.toUser(): User = User(
    name = this.name,
    email = this.email,
    lastname = this.lastname ?: "",
    address = this.address ?: "",
    userImageUrl = this.userImageUrl ?: ""
)