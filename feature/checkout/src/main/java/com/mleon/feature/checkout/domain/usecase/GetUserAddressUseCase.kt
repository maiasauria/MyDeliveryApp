package com.mleon.feature.checkout.domain.usecase

import android.content.SharedPreferences
import com.mleon.core.data.datasource.remote.model.AuthResult
import com.mleon.core.data.repository.interfaces.UserRepository
import javax.inject.Inject

private const val USER_EMAIL_KEY = "user_email"
private const val ERROR_GETTING_ADDRESS = "Error al obtener la dirección del usuario."

class GetUserAddressUseCase @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): String {
        val email = sharedPreferences.getString(USER_EMAIL_KEY, null)
        val result = email?.let { userRepository.getUserByEmail(it) }
        return when (result) {
            is AuthResult.Success -> {
                result.user.address ?: throw NoAddressException()
            }

            else -> throw Exception(ERROR_GETTING_ADDRESS)
        }
    }
}
