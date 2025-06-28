package com.mleon.core.data.repository.interfaces

import com.mleon.core.data.model.LoginResult
import com.mleon.core.data.model.RegisterResult
import com.mleon.core.model.User
import com.mleon.core.model.dtos.UserDto

interface UserRepository {
    fun getUser(user: UserDto): User?
    fun saveUser(user: UserDto)
    fun deleteUser(user: UserDto)
    suspend fun registerUser(user: UserDto): RegisterResult
    suspend fun loginUser(email: String, password: String): LoginResult
    suspend fun getUserByEmail(email: String): User?
    suspend fun updateUser(user: User): User?
}