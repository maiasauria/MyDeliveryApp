package com.mleon.core.data.repository

import android.util.Log
import com.mleon.core.model.User
import com.mleon.core.model.UserDto
import com.mleon.core.data.model.LoginRequest
import com.mleon.core.data.model.LoginResult
import com.mleon.core.data.model.RegisterResult
import com.mleon.core.data.remote.ApiService
import org.json.JSONObject
import retrofit2.HttpException

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

    override suspend fun registerUser(user: UserDto): RegisterResult {
        return try {
            val response = apiService.registerUser(user)
            Log.d("UserRepositoryApi", "Register response: $response")
            if (response.message != null) {
                RegisterResult(user = null, message = response.message)
            } else if (response.email != null && response.name != null) {
                RegisterResult(
                    user = UserDto(
                        name = response.name,
                        email = response.email,
                        password = "" // Do not expose password
                    ),
                    message = "User registered successfully"
                )
            } else {
                RegisterResult(user = null, message = "Registration failed")
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val message = JSONObject(errorBody ?: "{}").optString("message", "Unknown error")
            RegisterResult(user = null, message = message)
        } catch (e: Exception) {
            RegisterResult(user = null, message = "Error: ${e.message}")
        }
    }

    override suspend fun loginUser(email: String, password: String): LoginResult {
        return try {
            val response = apiService.loginUser(LoginRequest(email, password))
            Log.d("UserRepositoryApi", "Login response: $response")
            val user = response.user
            if (user != null) {
                LoginResult(
                    user = UserDto(name = user.name, email = user.email, password = ""), // Do not expose password),
                    message = response.message
                )
            } else {
                LoginResult(user = null, message = response.message ?: "Login failed")
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val message = JSONObject(errorBody ?: "{}").optString("message", "Error desconocido")
            LoginResult(user = null, message = message)
        } catch (e: Exception) {
            LoginResult(user = null, message = "Error: ${e.message}")
        }
    }
}