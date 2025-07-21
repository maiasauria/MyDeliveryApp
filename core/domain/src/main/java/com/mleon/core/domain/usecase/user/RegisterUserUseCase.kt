package com.mleon.core.domain.usecase.user

import com.mleon.core.data.repository.interfaces.UserRepository
import com.mleon.core.model.result.AuthResult
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    // Puede ser suspend para operaciones asíncronas.
    suspend operator fun invoke(name: String, lastname: String, email:String, password:String): AuthResult {

        return userRepository.registerUser(
            name = name,
            lastname = lastname,
            email = email,
            password = password)
    }
}