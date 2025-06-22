package com.mleon.mydeliveryapp.data.repository

import com.mleon.core.model.User
import com.mleon.core.model.UserDto
import com.mleon.mydeliveryapp.data.model.LoginResult
import com.mleon.mydeliveryapp.data.model.RegisterResult

interface UserRepository {
    fun getUser(user: UserDto): User?
    fun saveUser(user: UserDto)
    fun deleteUser(user: UserDto)
    suspend fun registerUser(user: UserDto): RegisterResult
    suspend fun loginUser(email: String, password: String): LoginResult
}