package com.mleon.mydeliveryapp.data.repository

import com.mleon.core.model.User
import com.mleon.core.model.UserDto

interface UserRepository {
    fun getUser(user: UserDto): User?
    fun saveUser(user: UserDto)
    fun deleteUser(user: UserDto)
    fun registerUser(user: UserDto): User?
    fun loginUser(email: String, password: String): UserDto?
}