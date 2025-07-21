package com.mleon.core.data.datasource.remote.service

import com.mleon.core.data.datasource.remote.dto.UserDto
import com.mleon.core.data.datasource.remote.model.LoginRequest
import com.mleon.core.data.datasource.remote.model.LoginResponse
import com.mleon.core.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UsersApiService {
    @POST("users/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): LoginResponse

    @POST("users/register")
    suspend fun registerUser(@Body registerRequest: UserDto): UserDto

    @GET("users/{email}")
    suspend fun getUserByEmail(@Path("email") email: String): UserDto

    @PUT("users/update/{email}")
    suspend fun updateUser(@Path("email") email:String, @Body user: User): UserDto
}
