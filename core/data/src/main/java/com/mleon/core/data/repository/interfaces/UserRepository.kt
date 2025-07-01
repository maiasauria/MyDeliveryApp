package com.mleon.core.data.repository.interfaces

import com.mleon.core.data.model.LoginResult
import com.mleon.core.data.model.RegisterResult
import com.mleon.core.model.User

interface UserRepository {

    //Le paso los parámetros y que el repositorio se encargue de crear el DTO
    suspend fun registerUser(name: String, lastname: String, email: String, password: String) : RegisterResult
    suspend fun loginUser(email: String, password: String): LoginResult
    suspend fun getUserByEmail(email: String): User?
    suspend fun updateUser(user: User): User?
}