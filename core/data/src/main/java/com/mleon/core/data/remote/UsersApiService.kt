package com.mleon.core.data.remote

import com.mleon.core.data.model.LoginRequest
import com.mleon.core.data.model.LoginResponse
import com.mleon.core.data.model.RegisterResponse
import com.mleon.core.model.UserDto
import retrofit2.http.Body
import retrofit2.http.POST

interface UsersApiService {
    @POST("users/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): LoginResponse

    @POST("users/register")
    suspend fun registerUser(@Body registerRequest: UserDto): RegisterResponse
}