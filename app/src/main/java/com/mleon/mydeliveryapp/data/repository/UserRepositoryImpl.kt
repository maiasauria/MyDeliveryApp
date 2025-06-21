package com.mleon.mydeliveryapp.data.repository

import com.mleon.core.model.DatabaseUser
import com.mleon.core.model.User
import com.mleon.core.model.UserDto
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor() : UserRepository  {
    private val users: MutableList<DatabaseUser> = mutableListOf(
        DatabaseUser(1, "Nicolas", "nicolas@gmail.com", "password123"),
        DatabaseUser(2, "Andrea", "a@a.com", "password456"),
    )
    private var nextId = 3

    override fun getUser(user: UserDto): User? {
        val dbUser = users.find { it.email == user.email }
        return dbUser?.let { User(it.name, it.email, it.password) }
    }

    override fun saveUser(user: UserDto) {
        if (users.none { it.email == user.email }) {
            users.add(DatabaseUser(nextId++, user.name, user.email, user.password))
        }
    }

    override fun deleteUser(user: UserDto) {
        users.removeIf { it.email == user.email }
    }

    override fun registerUser(user: UserDto): User? {
        if (users.any { it.email == user.email }) return null // Email already exists
        val dbUser = DatabaseUser(
            id = nextId++,
            name = user.name,
            email = user.email,
            password = user.password
        )
        users.add(dbUser)
        return User(dbUser.name, dbUser.email, dbUser.password)
    }

    override fun loginUser(email: String, password: String): UserDto? {
        val dbUser = users.find { it.email == email && it.password == password }
        return dbUser?.let { UserDto(it.name, it.email, it.password) }
    }

}
