package com.mleon.core.data.datasource.fake

import android.util.Log
import com.mleon.core.data.datasource.UserDataSource
import com.mleon.core.data.model.LoginResult
import com.mleon.core.data.model.RegisterResult
import com.mleon.core.model.DatabaseUser
import com.mleon.core.model.User
import com.mleon.core.model.toUser
import javax.inject.Inject

class UserFakeDataSource
    @Inject
    constructor() : UserDataSource {
        private val users: MutableList<DatabaseUser> =
            mutableListOf(
                DatabaseUser(1, "Nicolas", "nicolas@gmail.com", "password123"),
                DatabaseUser(2, "Andrea", "a@a.com", "password456"),
            )
        private var nextId = 3

    override suspend fun registerUser(
        name: String,
        lastname: String,
        email: String,
        password: String
    ): RegisterResult {
        if (users.any { it.email == email }) {
            return RegisterResult.Error(errorMessage = "Email already exists")
        }
        val dbUser = DatabaseUser(
            id = nextId++,
            name = name,
            email = email,
            password = password,
        )
        users.add(dbUser)
        return RegisterResult.Success(
            user = dbUser.toUser(),
            message = "User registered successfully"
        )
    }

        override suspend fun loginUser(
            email: String,
            password: String,
        ): LoginResult {
            Log.d("UserRepositoryImpl", "Attempting login for email: $email")
            val dbUser = users.find { it.email == email && it.password == password }
            return if (dbUser != null) { LoginResult.Success(
                    user = dbUser.toUser(),
                    message = "Sesion iniciada",
                )
            } else {
                LoginResult.Error(
                    errorMessage = "Email o contraseña inválidos",
                )
            }
        }

        override suspend fun getUserByEmail(email: String): User? {
            Log.d("UserRepositoryImpl", "Fetching user by email: $email")
            val dbUser = users.find { it.email == email }
            return dbUser?.toUser()
        }

        override suspend fun updateUser(user: User): User? {
            Log.d("UserRepositoryImpl", "Updating user: $user")
            val index = users.indexOfFirst { it.email == user.email }
            return if (index != -1) {
                val updatedUser =
                    DatabaseUser(
                        id = users[index].id,
                        name = user.name,
                        email = user.email,
                        password = users[index].password,
                    )
                users[index] = updatedUser
                updatedUser.toUser()
            } else {
                null
            }
        }
    }
