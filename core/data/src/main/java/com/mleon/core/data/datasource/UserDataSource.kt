package com.mleon.core.data.datasource

import com.mleon.core.data.model.LoginResult
import com.mleon.core.data.model.RegisterResult
import com.mleon.core.model.User

interface UserDataSource {
    suspend fun getUserByEmail(email: String): User?
    suspend fun registerUser(name: String, lastname: String, email: String, password: String): RegisterResult
    suspend fun loginUser(email: String, password: String): LoginResult
    suspend fun updateUser(user: User): User?
}