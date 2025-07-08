package com.mleon.feature.signup.usecase

import com.mleon.core.data.model.RegisterResult
import com.mleon.core.data.repository.interfaces.UserRepository
import javax.inject.Inject

data class RegisterUserParams(
    val name: String,
    val lastname: String,
    val email: String,
    val password: String
)

class RegisterUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    // Puede ser suspend para operaciones asíncronas.
    suspend operator fun invoke(params: RegisterUserParams): RegisterResult {

        return userRepository.registerUser(
            name = params.name,
            lastname = params.lastname,
            email = params.email,
            password = params.password)
    }
}