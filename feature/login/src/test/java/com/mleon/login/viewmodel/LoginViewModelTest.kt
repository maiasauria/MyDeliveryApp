package com.mleon.login.viewmodel

import android.content.SharedPreferences
import android.util.Log
import com.mleon.core.domain.usecase.user.LoginUserUseCase
import com.mleon.core.domain.usecase.user.SaveUserEmailUseCase
import com.mleon.core.model.User
import com.mleon.core.model.result.AuthResult
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var loginUserUseCase: LoginUserUseCase
    private lateinit var saveUserEmailUseCase: SaveUserEmailUseCase
    private lateinit var viewModel: LoginViewModel
    private val testScheduler = TestCoroutineScheduler()

    @Before
    fun setUp() {
        sharedPreferences = mockk(relaxed = true)
        loginUserUseCase = mockk()
        saveUserEmailUseCase = mockk(relaxed = true)
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
        every { Log.d(any(), any(), any()) } returns 0
    }

    @Test
    fun `uiState is invalid when email is invalid`() {
        viewModel = LoginViewModel(
            loginUserUseCase,
            saveUserEmailUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.onEmailChange("invalid-email")
        thenUiStateIsInvalidEmail(viewModel)
    }

    @Test
    fun `uiState is invalid when password is too short`() {
        viewModel = LoginViewModel(
            loginUserUseCase,
            saveUserEmailUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.onPasswordChange("short")
        thenUiStateIsInvalidPassword(viewModel)
    }

    @Test
    fun `uiState is valid when email and password are valid`() {
        viewModel = LoginViewModel(
            loginUserUseCase,
            saveUserEmailUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.onEmailChange("test@example.com")
        viewModel.onPasswordChange("password1")
        thenUiStateIsFormValid(viewModel)
    }

    @Test
    fun `uiState is error when login fails`() = runTest {
        val errorMessage = "Invalid credentials"
        val errorCode = 401
        coEvery { loginUserUseCase(any() , any()) } returns AuthResult.Error(errorMessage, errorCode)
        viewModel = LoginViewModel(
            loginUserUseCase,
            saveUserEmailUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.onEmailChange("test@example.com")
        viewModel.onPasswordChange("password1")
        viewModel.onLoginClick()
        advanceUntilIdle()
        thenUiStateIsLoginError(viewModel, errorMessage)
    }

    @Test
    fun `uiState is success and email is saved when login succeeds`() = runTest {
        val user = User(email = "test@example.com", name = "Test User", lastname = "User", address = "123 Test St")
        coEvery { loginUserUseCase(any(), any()) } returns AuthResult.Success(message = "", user = user)
        every { saveUserEmailUseCase.invoke(user.email) } returns Unit
        viewModel = LoginViewModel(
            loginUserUseCase,
            saveUserEmailUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.onEmailChange(user.email)
        viewModel.onPasswordChange("password1")
        viewModel.onLoginClick()
        advanceUntilIdle()
        thenUiStateIsLoginSuccess(viewModel, user.email)
        verify(exactly = 1) { saveUserEmailUseCase.invoke(user.email) }
    }

    // Helpers

    private fun thenUiStateIsInvalidEmail(viewModel: LoginViewModel) {
        val state = viewModel.uiState.value
        assertFalse(state.isEmailValid)
        assertEquals("El email no es válido", state.errorMessageEmail)
    }

    private fun thenUiStateIsInvalidPassword(viewModel: LoginViewModel) {
        val state = viewModel.uiState.value
        assertFalse(state.isPasswordValid)
        assertEquals("La contraseña debe tener entre 8 y 12 caracteres", state.errorMessagePassword)
    }

    private fun thenUiStateIsFormValid(viewModel: LoginViewModel) {
        val state = viewModel.uiState.value
        assertTrue(state.isFormValid)
    }

    private fun thenUiStateIsLoginError(viewModel: LoginViewModel, errorMessage: String) {
        val state = viewModel.uiState.value
        assertFalse(state.loginSuccess)
        assertEquals(errorMessage, state.errorMessageLogin)
        assertFalse(state.isLoading)
    }

    private fun thenUiStateIsLoginSuccess(viewModel: LoginViewModel, email: String) {
        val state = viewModel.uiState.value
        assertTrue(state.loginSuccess)
        assertEquals("", state.errorMessageLogin)
        assertEquals("", state.password)
    }
}
