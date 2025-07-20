package com.mleon.feature.signup.usecase

import android.content.SharedPreferences
import javax.inject.Inject

class SaveUserEmailUseCase @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    operator fun invoke(email: String) {
        println("Saving user email: $email")
        sharedPreferences.edit().putString(KEY_USER_EMAIL, email).apply()
    }

    companion object {
        private const val KEY_USER_EMAIL = "user_email"
    }
}
