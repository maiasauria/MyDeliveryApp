package com.mleon.core.data.remote

import com.mleon.core.data.model.LoginRequest
import com.mleon.core.data.model.LoginResponse
import com.mleon.core.data.model.RegisterResponse
import com.mleon.core.model.User
import com.mleon.core.model.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UsersApiService {
    @POST("users/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): LoginResponse

    @POST("users/register")
    suspend fun registerUser(@Body registerRequest: UserDto): RegisterResponse

    @GET("users/{email}")
    suspend fun getUserByEmail(@Path("email") email: String): UserDto

    @PUT("users/update/{email}")
    suspend fun updateUser(@Path("email") email:String, @Body user: User): UserDto
}
