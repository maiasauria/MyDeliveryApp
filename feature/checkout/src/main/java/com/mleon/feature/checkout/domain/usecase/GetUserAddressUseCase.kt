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
        when (result) {
            is AuthResult.Success -> {
                return result.user.address
                    ?: throw Exception("No se encuentra la dirección del usuario")
            }

            is AuthResult.Error -> {
                throw Exception("Error al obtener la dirección del usuario.")
            }

            else -> {
                throw Exception("No se pudo obtener la dirección del usuario.")
            }
        }
    }
}