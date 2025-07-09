package com.mleon.core.data.remote

import com.mleon.core.data.datasource.remote.model.UserDto
import com.mleon.core.data.model.AuthResponse
import com.mleon.core.data.model.LoginRequest
import com.mleon.core.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UsersApiService {
    @POST("users/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): AuthResponse

    @POST("users/register")
    suspend fun registerUser(@Body registerRequest: UserDto): AuthResponse

    @GET("users/{email}")
    suspend fun getUserByEmail(@Path("email") email: String): UserDto

    @PUT("users/update/{email}")
    suspend fun updateUser(@Path("email") email:String, @Body user: User): UserDto
}
