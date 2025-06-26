package com.mleon.core.data.datasource.remote

import android.util.Log
import com.mleon.core.data.model.LoginRequest
import com.mleon.core.data.model.LoginResult
import com.mleon.core.data.model.RegisterResult
import com.mleon.core.data.remote.UsersApiService
import com.mleon.core.data.repository.interfaces.UserRepository
import com.mleon.core.model.User
import com.mleon.core.model.UserDto
import com.mleon.core.model.toUser
import org.json.JSONObject
import retrofit2.HttpException

class UserRemoteDataSource(private val apiService: UsersApiService) : UserRepository {

    override fun getUser(user: UserDto): User? {
        return User(
            email = user.email,
            name = user.name,
            lastname = user.lastname,
            address = user.address,
            userImageUrl = user.userImageUrl
        )
    }

    override fun saveUser(user: UserDto) {
        // Implementation for saving user details to the API
    }

    override fun deleteUser(user: UserDto) {
        // Implementation for deleting user from the API
    }

    override suspend fun registerUser(user: UserDto): RegisterResult {
        return try {
            Log.d("UserRepositoryApi", "Registering user: $user")
            val response = apiService.registerUser(user)
            Log.d("UserRepositoryApi", "Register response: $response")
            if (response.message != null) {
                RegisterResult(user = null, message = response.message)
            } else if (
                response.email != null &&
                response.name != null &&
                response.lastname != null
            ) {
                RegisterResult(
                    user = User(name = user.name, email = user.email,
                        lastname = user.lastname, address = "",
                    ),
                    message = "User registered successfully"
                )
            } else {
                RegisterResult(user = null, message = "Registration failed")
            }
        } catch (e: HttpException) {
            Log.e("UserRepositoryApi", "HTTP error during registration: ${e.message()}")
            val errorBody = e.response()?.errorBody()?.string()
            val message = JSONObject(errorBody ?: "{}").optString("message", "Unknown error")
            RegisterResult(user = null, message = message)
        } catch (e: Exception) {
            Log.e("UserRepositoryApi", "Error during registration: ${e.message}")
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
                    user = User(name = user.name, email = user.email,
                        lastname = user.lastname, address = user.address,
                        ), // Do not expose password),
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

    override suspend fun getUserByEmail(email: String): User? {
        return try {
            val userDto = apiService.getUserByEmail(email)
            userDto?.toUser()
        } catch (e: HttpException) {
            Log.e("UserRepositoryApi", "Error fetching user by email: ${e.message()}")
            null
        } catch (e: Exception) {
            Log.e("UserRepositoryApi", "Error fetching user by email: ${e.message}")
            null
        }
    }

    override suspend fun updateUser(user: User): User? {
        return try {
            val response = apiService.updateUser(user.email, user)
            response.toUser()
        } catch (e: HttpException) {
            Log.e("UserRepositoryApi", "Error updating user: ${e.message()}")
            null
        } catch (e: Exception) {
            Log.e("UserRepositoryApi", "Error updating user: ${e.message}")
            null
        }
    }


}
