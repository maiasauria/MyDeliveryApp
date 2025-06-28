package com.mleon.core.data.datasource.fake

import android.util.Log
import com.mleon.core.data.model.LoginResult
import com.mleon.core.data.model.RegisterResult
import com.mleon.core.data.repository.interfaces.UserRepository
import com.mleon.core.model.DatabaseUser
import com.mleon.core.model.User
import com.mleon.core.model.dtos.UserDto
import com.mleon.core.model.toUser
import javax.inject.Inject

class UserFakeDataSource
    @Inject
    constructor() : UserRepository {
        private val users: MutableList<DatabaseUser> =
            mutableListOf(
                DatabaseUser(1, "Nicolas", "nicolas@gmail.com", "password123"),
                DatabaseUser(2, "Andrea", "a@a.com", "password456"),
            )
        private var nextId = 3

        override fun getUser(user: UserDto): User? {
            val dbUser = users.find { it.email == user.email }
            return dbUser?.toUser()
        }

        override fun saveUser(user: UserDto) {
            if (users.none { it.email == user.email }) {
                users.add(DatabaseUser(nextId++, user.name, user.email, user.password))
            }
        }

        override fun deleteUser(user: UserDto) {
            users.removeIf { it.email == user.email }
        }

        override suspend fun registerUser(user: UserDto): RegisterResult {
            if (users.any { it.email == user.email }) {
                return RegisterResult(
                    user = null,
                    message = "Email already exists",
                )
            }
            val dbUser =
                DatabaseUser(
                    id = nextId++,
                    name = user.name,
                    email = user.email,
                    password = user.password,
                )
            users.add(dbUser)
            return RegisterResult(
                user = dbUser.toUser(),
                message = "User registered successfully",
            )
        }

        override suspend fun loginUser(
            email: String,
            password: String,
        ): LoginResult {
            Log.d("UserRepositoryImpl", "Attempting login for email: $email")
            val dbUser = users.find { it.email == email && it.password == password }
            return if (dbUser != null) {
                LoginResult(
                    user = dbUser.toUser(),
                    message = "Login successful",
                )
            } else {
                LoginResult(
                    user = null,
                    message = "Invalid email or password",
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
