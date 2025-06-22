package com.mleon.core.data.remote

import com.mleon.core.model.UserDto
import com.mleon.core.data.model.LoginRequest
import com.mleon.core.data.model.LoginResponse
import com.mleon.core.data.model.RegisterResponse
import com.mleon.core.data.model.RemoteProduct
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("foods")
    suspend fun getProducts(): List<RemoteProduct>

    @POST("users/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): LoginResponse

    @POST("users/register")
    suspend fun registerUser(@Body registerRequest: UserDto): RegisterResponse
}