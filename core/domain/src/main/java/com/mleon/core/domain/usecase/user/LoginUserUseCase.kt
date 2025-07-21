package com.mleon.core.domain.usecase.user

import com.mleon.core.data.repository.interfaces.UserRepository
import com.mleon.core.model.result.AuthResult
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): AuthResult {
        return userRepository.loginUser(email = email, password = password)
    }
}
