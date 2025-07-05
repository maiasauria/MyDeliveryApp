package com.mleon.feature.profile.usecase

import android.content.SharedPreferences
import com.mleon.core.data.repository.interfaces.UserRepository
import com.mleon.core.model.User
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val sharedPreferences: SharedPreferences
) {
    suspend operator fun invoke(): User? {
        val userEmail = sharedPreferences.getString("user_email", null)
        if (userEmail.isNullOrEmpty()) {
            return null
        }
        return userRepository.getUserByEmail(userEmail)
    }
}