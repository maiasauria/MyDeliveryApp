package com.mleon.core.domain.usecase.user

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class LogoutUserUseCase @Inject constructor (private val sharedPreferences: SharedPreferences) {
    operator fun invoke() {
        sharedPreferences.edit { clear() }
    }
}