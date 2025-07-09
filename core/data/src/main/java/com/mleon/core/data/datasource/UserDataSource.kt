package com.mleon.core.data.datasource

import com.mleon.core.data.model.AuthResult
import com.mleon.core.model.User

interface UserDataSource {
    suspend fun getUserByEmail(email: String): AuthResult
    suspend fun registerUser(name: String, lastname: String, email: String, password: String): AuthResult
    suspend fun loginUser(email: String, password: String): AuthResult
    suspend fun updateUser(user: User): AuthResult
}