package com.mleon.core.data.datasource

import com.mleon.core.data.model.LoginResult
import com.mleon.core.data.model.RegisterResult
import com.mleon.core.model.User
import com.mleon.core.model.dtos.UserDto

interface UserDataSource {
    suspend fun getUserByEmail(email: String): User?
    suspend fun registerUser(user: UserDto): RegisterResult
    suspend fun loginUser(email: String, password: String): LoginResult
    suspend fun updateUser(user: User): User?
}