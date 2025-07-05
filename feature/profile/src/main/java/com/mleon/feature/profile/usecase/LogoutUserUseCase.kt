package com.mleon.feature.profile.usecase

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class LogoutUserUseCase @Inject constructor (private val sharedPreferences: SharedPreferences) {
    operator fun invoke() {
        sharedPreferences.edit { clear() }
    }
}