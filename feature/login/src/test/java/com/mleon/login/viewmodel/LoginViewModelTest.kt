package com.mleon.login.viewmodel

import android.content.SharedPreferences
import android.util.Log
import com.mleon.core.data.domain.LoginUserUseCase
import com.mleon.core.data.model.LoginResult
import com.mleon.core.model.User
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var loginUserUseCase: LoginUserUseCase
    private lateinit var viewModel: LoginViewModel
    private val testScheduler = TestCoroutineScheduler()

    @Before
    fun setUp() {
        sharedPreferences = mock()
        loginUserUseCase = mock()
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
        every { Log.d(any(), any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given invalid email when onEmailChange then email validation fails`() {
        val invalidEmail = "invalid-email"
        viewModel = LoginViewModel(
            sharedPreferences,
            loginUserUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.onEmailChange(invalidEmail)
        assertFalse(viewModel.uiState.value.isEmailValid)
        assertEquals("El email no es válido", viewModel.uiState.value.errorMessageEmail)


    }

    @Test
    fun `given short password when onPasswordChange then password validation fails`() {
        val shortPassword = "short"
        viewModel = LoginViewModel(
            sharedPreferences,
            loginUserUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.onPasswordChange(shortPassword)
        assertFalse(viewModel.uiState.value.isPasswordValid)
        assertEquals(
            "La contraseña debe tener entre 8 y 12 caracteres",
            viewModel.uiState.value.errorMessagePassword
        )
    }

    @Test
    fun `given valid email and password when both changed then form is valid`() = runTest {
        val email = "test@example.com"
        val password = "password1"
        viewModel = LoginViewModel(
            sharedPreferences,
            loginUserUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)
        assertTrue(viewModel.uiState.value.isFormValid)
    }

    @Test
    fun `onLoginClick with invalid credentials updates state with error message`() = runTest {
        val errorMessage = "Invalid credentials"
        val errorCode = 401
        val result = LoginResult.Error(errorMessage, errorCode)
        whenever(loginUserUseCase(any())).thenReturn(result)
        viewModel = LoginViewModel(
            sharedPreferences,
            loginUserUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.onEmailChange("test@example.com")
        viewModel.onPasswordChange("password1")
        viewModel.onLoginClick()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.loginSuccess)
        assertEquals(errorMessage, state.errorMessageLogin)
        assertFalse(state.isLoading)
    }

    @Test
    fun `onLoginClick with valid credentials updates state and saves email`() = runTest {
        val user = User(email = "test@example.com", name = "Test User", lastname = "User", address = "123 Test St")
        val result = LoginResult.Success(message = "", user = user)
        val editor: SharedPreferences.Editor = mock()
        whenever(sharedPreferences.edit()).thenReturn(editor)
        whenever(editor.putString(any(), any())).thenReturn(editor)
        whenever(loginUserUseCase(any())).thenReturn(result)
        viewModel = LoginViewModel(
            sharedPreferences,
            loginUserUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.onEmailChange(user.email)
        viewModel.onPasswordChange("password1")
        viewModel.onLoginClick()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        println("State after login: $state")
        assertTrue(state.loginSuccess)
        assertEquals("", state.errorMessageLogin)
        assertEquals("", state.password)
        verify(editor).putString(eq("user_email"), eq(user.email))
        verify(editor).apply()
    }
}

