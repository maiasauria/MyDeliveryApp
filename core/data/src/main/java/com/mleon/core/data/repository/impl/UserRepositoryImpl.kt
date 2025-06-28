package com.mleon.core.data.repository.impl

import com.mleon.core.data.datasource.UserDataSource
import com.mleon.core.data.model.LoginResult
import com.mleon.core.data.model.RegisterResult
import com.mleon.core.data.repository.interfaces.UserRepository
import com.mleon.core.model.User
import com.mleon.core.model.dtos.UserDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
) : UserRepository {
    override suspend fun registerUser(user: UserDto): RegisterResult =
        withContext(Dispatchers.IO) {
            userDataSource.registerUser(user)
        }

    override suspend fun loginUser(
        email: String,
        password: String,
    ): LoginResult =
        withContext(Dispatchers.IO) {
            // return withContext to ensure the operation runs on the IO dispatcher
            userDataSource.loginUser(email, password)
        }

    override suspend fun getUserByEmail(email: String): User? =
        withContext(Dispatchers.IO) {
            userDataSource.getUserByEmail(email)
        }

    override suspend fun updateUser(user: User): User? {
        return withContext(Dispatchers.IO) {
            userDataSource.updateUser(user)
        }
    }
}
