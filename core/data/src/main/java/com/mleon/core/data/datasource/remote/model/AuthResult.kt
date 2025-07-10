package com.mleon.core.data.datasource.remote.model

import com.mleon.core.model.User

sealed class AuthResult {
    data class Success(val message: String, val user: User) : AuthResult()
    data class Error(val errorMessage: String, val errorCode: Int? = null) : AuthResult()
}