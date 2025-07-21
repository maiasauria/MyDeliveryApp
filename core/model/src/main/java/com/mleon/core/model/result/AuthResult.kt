package com.mleon.core.model.result

import com.mleon.core.model.User

sealed class AuthResult {
    data class Success(val message: String, val user: User) : AuthResult()
    data class Error(val errorMessage: String, val errorCode: Int? = null) : AuthResult()
}