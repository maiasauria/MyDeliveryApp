package com.mleon.login

import android.content.SharedPreferences
import com.mleon.core.data.domain.LoginUserUseCase
import com.mleon.login.viewmodel.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock


@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var loginUserUseCase: LoginUserUseCase
    private lateinit var viewModel: LoginViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        sharedPreferences = mock()
        loginUserUseCase = mock()
        viewModel = LoginViewModel(sharedPreferences, loginUserUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `given invalid email when onEmailChange then email validation fails`() {
        val invalidEmail = "invalid-email"

        viewModel.onEmailChange(invalidEmail)
        assertFalse(viewModel.uiState.value.isEmailValid)
        assertEquals("El email no es válido", viewModel.uiState.value.errorMessageEmail)


    }

    @Test
    fun `given short password when onPasswordChange then password validation fails`() {
        val shortPassword = "short"
        viewModel.onPasswordChange(shortPassword)
        assertFalse(viewModel.uiState.value.isPasswordValid)
        assertEquals(
            "La contraseña debe tener entre 8 y 12 caracteres",
            viewModel.uiState.value.errorMessagePassword
        )
    }

    @Test
    fun `given valid email and password when both changed then form is valid`() {
        val email = "test@example.com"
        val password = "password1"
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)
        assertTrue(viewModel.uiState.value.isFormValid)
    }
}