package com.mleon.login.usecase

import com.mleon.core.data.datasource.remote.model.AuthResult
import com.mleon.core.data.repository.interfaces.UserRepository
import javax.inject.Inject

data class LoginUserParams(
    val email: String,
    val password: String
)

class LoginUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(params: LoginUserParams): AuthResult {
        return userRepository.loginUser(email = params.email, password = params.password)
    }
}
