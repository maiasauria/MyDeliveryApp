package com.mleon.feature.profile.usecase

import com.mleon.core.data.repository.interfaces.UserRepository
import com.mleon.core.model.User
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(user: User) {
        userRepository.updateUser(user)
    }
}