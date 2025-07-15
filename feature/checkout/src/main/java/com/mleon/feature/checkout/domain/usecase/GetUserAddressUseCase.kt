package com.mleon.feature.checkout.domain.usecase


import android.content.SharedPreferences
import com.mleon.core.data.datasource.remote.model.AuthResult
import com.mleon.core.data.repository.interfaces.UserRepository
import javax.inject.Inject

class GetUserAddressUseCase @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): String {
        val email = sharedPreferences.getString("user_email", null)
        val result = email?.let { userRepository.getUserByEmail(it) }
        return when (result) {
            is AuthResult.Success -> {
                result.user.address ?: throw NoAddressException()
            }
            else -> throw Exception("Error al obtener la dirección del usuario.")

        }
    }
}
class NoAddressException : Exception("No se encuentra la dirección del usuario")
