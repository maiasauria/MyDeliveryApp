package com.mleon.core.data.repository.interfaces

import com.mleon.core.data.model.AuthResult
import com.mleon.core.model.User

interface UserRepository {

    //Le paso los parámetros y que el repositorio se encargue de crear el DTO
    suspend fun registerUser(name: String, lastname: String, email: String, password: String) : AuthResult
    suspend fun loginUser(email: String, password: String): AuthResult
    suspend fun getUserByEmail(email: String): AuthResult
    suspend fun updateUser(user: User): AuthResult
}