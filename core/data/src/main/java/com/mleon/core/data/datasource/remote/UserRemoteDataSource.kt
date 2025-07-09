package com.mleon.core.data.datasource.remote

import com.mleon.core.data.datasource.UserDataSource
import com.mleon.core.data.datasource.remote.model.UserDto
import com.mleon.core.data.datasource.remote.model.toUser
import com.mleon.core.data.model.AuthResponse
import com.mleon.core.data.model.AuthResult
import com.mleon.core.data.model.LoginRequest
import com.mleon.core.data.remote.UsersApiService
import com.mleon.core.model.User
import retrofit2.HttpException
import java.io.IOException

private const val LOGIN_OK = "Usuario logueado exitosamente"
private const val REGISTER_OK = "Usuario registrado exitosamente"
private const val UPDATE_OK = "Usuario actualizado exitosamente"
private const val ERROR_LOGIN = "Error al iniciar sesión"
private const val ERROR_REGISTER_USER = "Error al registrar usuario"
private const val ERROR_CONFLICT = "El usuario ya existe"
private const val ERROR_NOT_FOUND = "No se encontró el usuario"
private const val ERROR_INTERNAL_SERVER = "Error interno del servidor"
private const val ERROR_INVALID_PASSWORD = "Contraseña incorrecta"
private const val ERROR_UPDATE_BAD_REQUEST = "Solicitud incorrecta"
private const val ERROR_UPDATE_UNKNOWN = "Error al actualizar usuario"
private const val ERROR_GET_BY_EMAIL_UNKNOWN = "Error al buscar usuario"
private const val ERROR_UNKNOWN = "Ocurrió un error inesperado"
private const val ERROR_OFFLINE = "Sin conexión a Internet"

class UserRemoteDataSource(
    private val apiService: UsersApiService,
) : UserDataSource {
    override suspend fun registerUser(
        name: String,
        lastname: String,
        email: String,
        password: String
    ): AuthResult =
        try {
            val userDto = UserDto(
                name = name,
                lastname = lastname,
                email = email,
                password = password,
                address = "",
                userImageUrl = null
            )
            val response = apiService.registerUser(userDto)
            handleRegisterResponse(response)
        } catch (e: HttpException) {
            handleRegisterHttpException(e)
        } catch (e: Exception) {
            AuthResult.Error(ERROR_REGISTER_USER)
        }

    override suspend fun loginUser(email: String, password: String): AuthResult {
        return try {
            val loginRequest = LoginRequest(email, password)
            val responseBody: AuthResponse = apiService.loginUser(loginRequest)
            handleLoginResponse(responseBody)
        } catch (e: IOException) {
            AuthResult.Error(ERROR_OFFLINE)
        } catch (e: HttpException) {
            handleLoginHttpException(e)
        } catch (e: Exception) {
            AuthResult.Error(ERROR_UNKNOWN)
        }
    }

    override suspend fun getUserByEmail(email: String): AuthResult =
        try {
            val userDto = apiService.getUserByEmail(email)
            val user = userDto.toUser()
            AuthResult.Success(user = user, message = "")
        } catch (e: IOException) {
            AuthResult.Error(ERROR_OFFLINE)
        } catch (e: HttpException) {
            handleGetUserByEmailHttpException(e)
        } catch (e: Exception) {
            AuthResult.Error(ERROR_UNKNOWN)
        }

    override suspend fun updateUser(user: User): AuthResult =
        try {
            val response = apiService.updateUser(user.email, user)
            val updatedUser = response.toUser()
            AuthResult.Success(user = updatedUser, message = UPDATE_OK)
        } catch (e: IOException) {
            AuthResult.Error(ERROR_OFFLINE)
        } catch (e: HttpException) {
            handleUpdateUserHttpException(e)
        } catch (e: Exception) {
            AuthResult.Error(ERROR_UNKNOWN)
        }

    // Funciones auxiliares para manejar las respuestas y excepciones

    // Maneja los resultados de inicio de sesión
    private fun handleLoginResponse(responseBody: AuthResponse): AuthResult {
        return if (responseBody.user != null) {
            val domainUser: User = responseBody.user.toUser()
            AuthResult.Success(user = domainUser, message = LOGIN_OK)
        } else
        AuthResult.Error(ERROR_LOGIN)
    }

    private fun handleRegisterResponse(response: AuthResponse): AuthResult {
        return response.toUser()?.let { user ->
            AuthResult.Success(user = user, message = REGISTER_OK)
        } ?: AuthResult.Error(ERROR_REGISTER_USER)
    }

    // Maneja las excepciones HTTP específicas
    private fun handleRegisterHttpException(e: HttpException): AuthResult {
        val code = e.code()
        val errorMsg = when (code) {
            409 -> ERROR_CONFLICT
            500 -> ERROR_INTERNAL_SERVER
            else -> ERROR_REGISTER_USER
        }
        return AuthResult.Error(errorMsg,code)
    }

    private fun handleLoginHttpException(e: HttpException): AuthResult {
        val code = e.code()
        val errorMsg = when (code) {
            404 -> ERROR_NOT_FOUND
            401 -> ERROR_INVALID_PASSWORD
            500 -> ERROR_INTERNAL_SERVER
            else -> ERROR_LOGIN
        }
        return AuthResult.Error(errorMsg,code)
    }

    private fun handleUpdateUserHttpException(e: HttpException): AuthResult {
        val code = e.code()
        val errorMsg = when (code) {
            400 -> ERROR_UPDATE_BAD_REQUEST
            404 -> ERROR_NOT_FOUND
            500 -> ERROR_INTERNAL_SERVER
            else -> ERROR_UPDATE_UNKNOWN
        }
        return AuthResult.Error(errorMsg,code)
    }

    private fun handleGetUserByEmailHttpException(e: HttpException): AuthResult {
        val code = e.code()
        val errorMsg = when (code) {
            404 -> ERROR_NOT_FOUND
            500 -> ERROR_INTERNAL_SERVER
            else -> ERROR_GET_BY_EMAIL_UNKNOWN
        }
        return AuthResult.Error(errorMsg,code)
    }
}
