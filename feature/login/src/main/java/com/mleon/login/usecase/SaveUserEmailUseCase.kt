package com.mleon.login.usecase

import android.content.SharedPreferences
import javax.inject.Inject

class SaveUserEmailUseCase @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    operator fun invoke(email: String) {
        sharedPreferences.edit().putString(KEY_USER_EMAIL, email).apply()
    }

    companion object {
        private const val KEY_USER_EMAIL = "user_email"
    }
}

