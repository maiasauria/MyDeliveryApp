package com.mleon.core.data.repository.impl

import com.mleon.core.data.datasource.UserDataSource
import com.mleon.core.model.User
import com.mleon.core.model.result.AuthResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryImplTest {

    private lateinit var userDataSource: UserDataSource
    private lateinit var repository: UserRepositoryImpl

    @Before
    fun setUp() {
        userDataSource = mock(UserDataSource::class.java)
        repository = UserRepositoryImpl(userDataSource)
    }

    @Test
    fun `registerUser delegates to data source and returns result`() = runTest {
        val expected = AuthResult.Success(
            message = "Usuario registrado correctamente",
            user = User(
                "1", "Alice", "alice@email.com",
                address = "",
                userImageUrl = ""
            )
        )
        `when`(userDataSource.registerUser("Alice", "Smith", "alice@email.com", "pass"))
            .thenReturn(expected)

        val result = repository.registerUser("Alice", "Smith", "alice@email.com", "pass")

        assertEquals(expected, result)
        verify(userDataSource).registerUser("Alice", "Smith", "alice@email.com", "pass")
    }

    @Test
    fun `loginUser delegates to data source and returns result`() = runTest {
        val expected = AuthResult.Success(
            message = "Login exitoso",
            user = User(
                "2", "Bob", "bob@email.com",
                address = "",
                userImageUrl = ""
            )
        )
        `when`(userDataSource.loginUser("bob@email.com", "pass")).thenReturn(expected)

        val result = repository.loginUser("bob@email.com", "pass")

        assertEquals(expected, result)
        verify(userDataSource).loginUser("bob@email.com", "pass")
    }

    @Test
    fun `getUserByEmail delegates to data source and returns result`() = runTest {
        val expected = AuthResult.Success(
            message = "Usuario encontrado",
            user = User("3", "Carol", "carol@email.com",address = "",
                userImageUrl = "")
        )
        `when`(userDataSource.getUserByEmail("carol@email.com")).thenReturn(expected)

        val result = repository.getUserByEmail("carol@email.com")

        assertEquals(expected, result)
        verify(userDataSource).getUserByEmail("carol@email.com")
    }

    @Test
    fun `updateUser delegates to data source and returns result`() = runTest {
        val user = User("4", "Dave", "dave@email.com",address = "",
            userImageUrl = "")
        val expected = AuthResult.Success(
            message = "Usuario actualizado",
            user = user
        )
        `when`(userDataSource.updateUser(user)).thenReturn(expected)

        val result = repository.updateUser(user)

        assertEquals(expected, result)
        verify(userDataSource).updateUser(user)
    }
}