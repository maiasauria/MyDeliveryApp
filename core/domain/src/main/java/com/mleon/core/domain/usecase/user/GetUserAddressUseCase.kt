package com.mleon.core.domain.usecase.user

import android.content.SharedPreferences
import com.mleon.core.data.repository.interfaces.UserRepository
import com.mleon.core.domain.exception.NoAddressException
import com.mleon.core.model.result.AuthResult
import javax.inject.Inject

private const val USER_EMAIL_KEY = "user_email"
private const val ERROR_GETTING_ADDRESS = "Error al obtener la dirección del usuario."

class GetUserAddressUseCase @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): String {
        val email = sharedPreferences.getString(USER_EMAIL_KEY, null)
            ?: throw Exception(ERROR_GETTING_ADDRESS)
        return when (val result = userRepository.getUserByEmail(email)) {
            is AuthResult.Success -> {
                result.user.address ?: throw NoAddressException()
            }
            else -> throw Exception(ERROR_GETTING_ADDRESS)
        }
    }
}