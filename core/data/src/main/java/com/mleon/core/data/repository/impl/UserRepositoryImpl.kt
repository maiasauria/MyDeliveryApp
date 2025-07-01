package com.mleon.core.data.repository.impl

import com.mleon.core.data.datasource.UserDataSource
import com.mleon.core.data.model.LoginResult
import com.mleon.core.data.model.RegisterResult
import com.mleon.core.data.repository.interfaces.UserRepository
import com.mleon.core.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource, //interfaz, no implementacion. la provee Hilt.
) : UserRepository {
    override suspend fun registerUser(name: String, lastname: String, email: String, password: String) : RegisterResult =
        withContext(Dispatchers.IO) { // Use withContext to ensure the operation runs on the IO dispatcher
             userDataSource.registerUser(name = name, lastname = lastname, email = email, password = password)
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
