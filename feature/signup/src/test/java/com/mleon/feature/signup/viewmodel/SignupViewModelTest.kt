package com.mleon.feature.signup.viewmodel

import android.util.Log
import com.mleon.core.data.datasource.remote.model.AuthResult
import com.mleon.core.model.User
import com.mleon.feature.signup.usecase.RegisterUserUseCase
import com.mleon.feature.signup.usecase.SaveUserEmailUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SignupViewModelTest {
    private lateinit var saveUserEmailUseCase: SaveUserEmailUseCase
    private lateinit var registerUserUseCase: RegisterUserUseCase
    private lateinit var viewModel: SignupViewModel
    private val testScheduler = TestCoroutineScheduler()

    @Before
    fun setUp() {
        saveUserEmailUseCase = mockk(relaxed = true)
        registerUserUseCase = mockk()
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
    }

    @Test
    fun `uiState is success and email is saved when signup succeeds`() = runTest {
        val user = mockk<User>(relaxed = true) { every { email } returns "a@a.com" }
        coEvery { registerUserUseCase(any()) } returns AuthResult.Success(user = user, message = "User registered successfully")
        every { saveUserEmailUseCase.invoke(user.email) } returns Unit
        viewModel = SignupViewModel(registerUserUseCase, saveUserEmailUseCase, StandardTestDispatcher(testScheduler))
        givenFillValidForm(viewModel)
        viewModel.onSignupClick()
        advanceUntilIdle()
        println("Signup success: ${viewModel.uiState.value}")
        thenUiStateIsSignupSuccess(viewModel, user.email)
        verify(atLeast = 1) { saveUserEmailUseCase.invoke("a@a.com") }
    }

    @Test
    fun `uiState is loading while waiting for signup result`() = runTest {
        coEvery { registerUserUseCase(any()) } coAnswers {
            kotlinx.coroutines.delay(100)
            AuthResult.Error("fail")
        }
        viewModel = SignupViewModel(registerUserUseCase, saveUserEmailUseCase, StandardTestDispatcher(testScheduler))
        givenFillValidForm(viewModel)
        viewModel.onSignupClick()
        advanceTimeBy(50)
        assertTrue(viewModel.uiState.value.isLoading)
        advanceUntilIdle()
    }

    @Test
    fun `uiState is error when signup fails`() = runTest {
        coEvery { registerUserUseCase(any()) } returns AuthResult.Error("Email already exists")
        viewModel = SignupViewModel(registerUserUseCase, saveUserEmailUseCase, StandardTestDispatcher(testScheduler))
        givenFillValidForm(viewModel)
        viewModel.onSignupClick()
        advanceUntilIdle()
        thenUiStateIsSignupError(viewModel, "Email already exists")
    }

    @Test
    fun `uiState shows validation errors and does not call use case when form is invalid`() = runTest {
        viewModel = SignupViewModel(registerUserUseCase, saveUserEmailUseCase, StandardTestDispatcher(testScheduler))
        viewModel.onNameChange("A")
        viewModel.onLastnameChange("S")
        viewModel.onEmailChange("bad")
        viewModel.onPasswordChange("123")
        viewModel.onConfirmPasswordChange("321")
        viewModel.onSignupClick()
        advanceUntilIdle()
        thenUiStateHasValidationErrors(viewModel)
        coVerify(exactly = 0) { registerUserUseCase(any()) }
    }

    @Test
    fun `uiState is error when signup throws exception`() = runTest {
        coEvery { registerUserUseCase(any()) } throws RuntimeException("Network error")
        viewModel = SignupViewModel(registerUserUseCase, saveUserEmailUseCase, StandardTestDispatcher(testScheduler))
        givenFillValidForm(viewModel)
        viewModel.onSignupClick()
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.errorMessageSignup.contains("Network error"))
    }

    @Test
    fun `uiState shows email validation error on invalid email`() = runTest {
        viewModel = SignupViewModel(registerUserUseCase, saveUserEmailUseCase, StandardTestDispatcher(testScheduler))
        viewModel.onEmailChange("invalid-email")
        assertEquals("El email no es válido", viewModel.uiState.value.errorMessageEmail)
    }

    @Test
    fun `uiState shows password validation error on short password`() = runTest {
        viewModel = SignupViewModel(registerUserUseCase, saveUserEmailUseCase, StandardTestDispatcher(testScheduler))
        viewModel.onPasswordChange("123")
        assertEquals("La contraseña debe tener entre 8 y 12 caracteres", viewModel.uiState.value.errorMessagePassword)
    }

    @Test
    fun `uiState shows confirm password validation error on mismatch`() = runTest {
        viewModel = SignupViewModel(registerUserUseCase, saveUserEmailUseCase, StandardTestDispatcher(testScheduler))
        viewModel.onPasswordChange("password12")
        viewModel.onConfirmPasswordChange("different")
        assertEquals("Las contraseñas no coinciden", viewModel.uiState.value.errorMessagePasswordConfirm)
    }

    // Helpers

    private fun givenFillValidForm(viewModel: SignupViewModel) {
        viewModel.onNameChange("Nombre")
        viewModel.onLastnameChange("Apellido")
        viewModel.onEmailChange("a@a.com")
        viewModel.onPasswordChange("password123")
        viewModel.onConfirmPasswordChange("password123")
    }

    private fun thenUiStateIsSignupSuccess(viewModel: SignupViewModel, email: String) {
        val state = viewModel.uiState.value
        assertTrue(state.signupSuccess)
        assertEquals("", state.errorMessageSignup)
        assertEquals("", state.password)
        assertEquals("", state.passwordConfirm)
    }

    private fun thenUiStateIsSignupError(viewModel: SignupViewModel, errorMessage: String) {
        val state = viewModel.uiState.value
        assertFalse(state.signupSuccess)
        assertEquals(errorMessage, state.errorMessageSignup)
    }

    private fun thenUiStateHasValidationErrors(viewModel: SignupViewModel) {
        val state = viewModel.uiState.value
        assertEquals("El nombre no puede estar vacío", state.errorMessageName)
        assertEquals("El apellido no puede estar vacío", state.errorMessageLastname)
        assertEquals("El email no es válido", state.errorMessageEmail)
        assertEquals("La contraseña debe tener entre 8 y 12 caracteres", state.errorMessagePassword)
        assertEquals("Las contraseñas no coinciden", state.errorMessagePasswordConfirm)
        assertEquals("Por favor, completa todos los campos correctamente.", state.errorMessageSignup)
    }
}