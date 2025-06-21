package com.mleon.mydeliveryapp.data.repository

import android.util.Log
import com.mleon.core.model.User
import com.mleon.core.model.UserDto
import com.mleon.mydeliveryapp.data.model.LoginRequest
import com.mleon.mydeliveryapp.data.remote.ApiService

class UserRepositoryApi(private val apiService: ApiService) : UserRepository {

    override fun getUser(user: UserDto): User? {
        // Implementation for fetching user details from the API
        return null // Placeholder return
    }

    override fun saveUser(user: UserDto) {
        // Implementation for saving user details to the API
    }

    override fun deleteUser(user: UserDto) {
        // Implementation for deleting user from the API
    }

    override suspend fun registerUser(user: UserDto): User? {
        val response = apiService.registerUser(user)
        Log.d("UserRepositoryApi", "Register response: $response")
        return if (response != null) {
            User(
                name = response.name,
                email = response.email,
                password = "" // Do not expose password
            )
        } else {
            null
        }
    }

    override suspend fun loginUser(email: String, password: String): UserDto? {
        val response = apiService.loginUser(LoginRequest(email, password))
        val user = response.user
        return if (user != null) {
            UserDto(
                name = user.name,
                email = user.email,
                password = "" // Do not expose password
            )
        } else {
            null
        }
    }
}