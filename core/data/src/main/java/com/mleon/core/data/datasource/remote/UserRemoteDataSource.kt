package com.mleon.core.data.datasource.remote

import android.util.Log
import com.mleon.core.data.datasource.UserDataSource
import com.mleon.core.data.datasource.remote.model.toUser
import com.mleon.core.data.model.LoginRequest
import com.mleon.core.data.model.LoginResponse
import com.mleon.core.data.model.LoginResult
import com.mleon.core.data.model.RegisterResponse
import com.mleon.core.data.model.RegisterResult
import com.mleon.core.data.remote.UsersApiService
import com.mleon.core.model.User
import com.mleon.core.model.dtos.UserDto
import com.mleon.core.model.dtos.toUser
import org.json.JSONObject
import retrofit2.HttpException

private const val LOGIN_OK = "Usuario logueado exitosamente"
private const val REGISTER_OK = "Usuario registrado exitosamente"
private const val ERROR_LOGIN = "Error al iniciar sesión"
private const val ERROR_REGISTER_USER = "Error al registrar usuario"
private const val ERROR_BAD_REQUEST = "Solicitud incorrecta"
private const val ERROR_CONFLICT = "El usuario ya existe"
private const val ERROR_NOT_FOUND = "No existe un usuario con ese email"
private const val ERROR_INTERNAL_SERVER = "Error interno del servidor"
private const val ERROR_UNKNOWN = "Error desconocido"
private const val ERROR_INVALID_PASSWORD = "Contraseña incorrecta"

class UserRemoteDataSource(
    private val apiService: UsersApiService,
) : UserDataSource { override suspend fun registerUser(name: String, lastname: String, email: String, password: String): RegisterResult =
        try {
            val userDto = UserDto(
                name = name, lastname = lastname, email = email, password = password, address = "", userImageUrl = null)
            val response = apiService.registerUser(userDto)
            handleRegisterResponse(userDto, response)
        } catch (e: HttpException) {
            handleRegisterHttpException(e)
        } catch (e: Exception) {
            Log.e("UserRemoteDataSource", "Error during registration: ${e.message}")
            RegisterResult.Error(errorMessage = ERROR_REGISTER_USER)
        }

    override suspend fun loginUser(email: String, password: String): LoginResult {
        return try {
            val loginRequest = LoginRequest(email, password)
            val responseBody: LoginResponse = apiService.loginUser(loginRequest)
            handleLoginResponse(responseBody)
        } catch (e: HttpException) {
            handleLoginHttpException(e)
        } catch (e: Exception) {
            Log.e("UserRemoteDataSource", "General Exception during login: ${e.message}", e)
            return LoginResult.Error(errorMessage = "Error inesperado al iniciar sesión: ${e.localizedMessage ?: ERROR_UNKNOWN}")
        }
    }

    override suspend fun getUserByEmail(email: String): User? =
        try {
            val userDto = apiService.getUserByEmail(email)
            userDto.toUser()
        } catch (e: HttpException) {
            Log.e("UserRemoteDataSource", "Error fetching user by email: ${e.message()}")
            null
        } catch (e: Exception) {
            Log.e("UserRemoteDataSource", "Error fetching user by email: ${e.message}")
            null
        }

    override suspend fun updateUser(user: User): User? =
        try {
            val response = apiService.updateUser(user.email, user)
            response.toUser()
        } catch (e: HttpException) {
            Log.e("UserRemoteDataSource", "Error updating user: ${e.message()}")
            null
        } catch (e: Exception) {
            Log.e("UserRemoteDataSource", "Error updating user: ${e.message}")
            null
        }


    // Funciones auxiliares para manejar las respuestas y excepciones

    // Maneja los resultados de inicio de sesión
    private fun handleLoginResponse(responseBody: LoginResponse): LoginResult {
        if (responseBody.user != null) {
            val domainUser: User = responseBody.user.toUser()
            return LoginResult.Success(
                message = responseBody.message,
                user = domainUser
            )
        }
        Log.w("UserRemoteDataSource", "Login HTTP call successful but no user data in response body.")
        return LoginResult.Error( errorMessage = ERROR_LOGIN)
    }



    // Maneja los resultados de registro
    private fun handleRegisterResponse(user: UserDto, response: RegisterResponse): RegisterResult {
        // Si la respuesta contiene los campos esperados, creamos el objeto User
        if (response.email != null && response.name != null && response.lastname != null) {
            return RegisterResult.Success(user = user.toUser(), message = REGISTER_OK)
        }
        // Si la respuesta HTTP fue exitosa, pero hay un mensaje
         if (response.message != null) {
            Log.e("UserRemoteDataSource", "Error during registration: ${response.message}")
            return RegisterResult.Error(errorMessage = response.message)
        }
        // Generic error
        return RegisterResult.Error(errorMessage = ERROR_REGISTER_USER)
    }

    private fun handleRegisterHttpException(e: HttpException): RegisterResult {
        val code = e.code()
        val errorBody = e.response()?.errorBody()?.string()
        val message = JSONObject(errorBody ?: "{}").optString("message", ERROR_UNKNOWN)
        Log.e("UserRemoteDataSource", "HTTP error during registration: $code - $message")
        // Maneja los errores HTTP específicos
        // Si el error es 400, 409, 404 o 500, devolvemos un mensaje específico
        val errorMsg = when (code) {
            400 -> ERROR_BAD_REQUEST
            409 -> ERROR_CONFLICT
            404 -> ERROR_NOT_FOUND
            500 -> ERROR_INTERNAL_SERVER
            else -> ERROR_REGISTER_USER
            }
        return RegisterResult.Error(errorCode = code, errorMessage = errorMsg)
    }

    private fun handleLoginHttpException(e: HttpException): LoginResult {
        val code = e.code()
        val errorBody = e.response()?.errorBody()?.string()
        val message = JSONObject(errorBody ?: "{}").optString("message", ERROR_UNKNOWN)
        Log.e("UserRemoteDataSource", "HTTP error during login: $code - $message")
        // Maneja los errores HTTP específicos
        val errorMsg = when (code) {
            401 -> ERROR_INVALID_PASSWORD
            404 -> ERROR_NOT_FOUND
            500 -> ERROR_INTERNAL_SERVER
            else -> ERROR_LOGIN
        }
        return LoginResult.Error(errorMessage = errorMsg, errorCode = code)
    }
}
