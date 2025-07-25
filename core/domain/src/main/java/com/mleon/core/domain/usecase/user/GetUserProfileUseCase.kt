package com.mleon.core.domain.usecase.user


import android.content.SharedPreferences
import com.mleon.core.data.repository.interfaces.UserRepository
import com.mleon.core.model.result.AuthResult
import javax.inject.Inject

private const val USER_EMAIL_KEY = "user_email"
private const val ERROR_MESSAGE = "Error al obtener el perfil de usuario"

class GetUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val sharedPreferences: SharedPreferences
) {

    suspend operator fun invoke(): AuthResult {
        val userEmail = sharedPreferences.getString(USER_EMAIL_KEY, null)
        if (userEmail.isNullOrEmpty()) {
            return AuthResult.Error(ERROR_MESSAGE)
        }
        return userRepository.getUserByEmail(userEmail)
    }
}