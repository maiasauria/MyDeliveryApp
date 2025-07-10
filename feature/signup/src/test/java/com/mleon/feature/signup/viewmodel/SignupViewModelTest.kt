package com.mleon.feature.signup.viewmodel

import android.content.SharedPreferences
import android.util.Log
import com.mleon.feature.signup.usecase.RegisterUserUseCase
import com.mleon.core.data.datasource.remote.model.AuthResult
import com.mleon.core.model.User
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
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

    private val sharedPreferences: SharedPreferences = mockk(relaxed = true)
    private val registerUserUseCase: RegisterUserUseCase = mockk()
    private lateinit var viewModel: SignupViewModel
    private val testScheduler = TestCoroutineScheduler()

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        mockSharedPreferences()
    }


    //CASOS VALIDOS

    @Test
    fun `onSignupButtonClick success updates uiState and saves email`() = runTest {

        // GIVEN: Se crea un caso de uso simulado que devuelve un resultado exitoso al registrar un usuario.
        val user = mockk<User>(relaxed = true) { every { email } returns "a@a.com" }
        coEvery { registerUserUseCase(any()) } returns AuthResult.Success(user = user, message = "User registered successfully")

        // No puedo usar el metodo createViewModel() porque no puedo pasarle el testDispatcher, por eso creo el viewModel directamente.
        viewModel = SignupViewModel(sharedPreferences, registerUserUseCase, StandardTestDispatcher(testScheduler))
        givenfillValidForm(viewModel)

        // WHEN: Se llama a onSignupButtonClick.
        viewModel.onSignupButtonClick()
        advanceUntilIdle()

        // THEN: Se verifica que el estado de la UI se actualiza correctamente y se guarda el email en SharedPreferences.
        val state = viewModel.uiState.value
        assertSignupSuccess(state)
        verify { sharedPreferences.edit() }
    }

    @Test
    fun `onSignupButtonClick sets isLoading true while waiting`() = runTest {

        // GIVEN Simula un caso de uso que tarda en completarse, retorno error pero es indistinto.
        coEvery { registerUserUseCase(any()) } coAnswers {
            delay(100) // Se simula un retraso porque no vamos a esperar un resultado
            AuthResult.Error("fail")
        }
        viewModel = createViewModel()
        //Seteo datos validos en el estado
        givenfillValidForm(viewModel)

        // WHEN
        viewModel.onSignupButtonClick()
        advanceTimeBy(50) // Avanza el tiempo para ver ese estado Loading

        //THEN
        assertTrue(viewModel.uiState.value.isLoading)
        advanceUntilIdle()
    }


    // CASOS INVALIDOS
    @Test
    fun `onSignupButtonClick error updates uiState with error message`() = runTest {
        // GIVEN: Se crea un caso de uso simulado que devuelve un error al registrar un usuario.
        coEvery { registerUserUseCase(any()) } returns AuthResult.Error("Email already exists")
        viewModel = SignupViewModel(sharedPreferences, registerUserUseCase, StandardTestDispatcher(testScheduler))
        givenfillValidForm(viewModel)

        // WHEN: Se llama al método onSignupButtonClick.
        viewModel.onSignupButtonClick()
        advanceUntilIdle()

        // THEN: Se verifica que el estado de la UI se actualiza con el mensaje de error.
        val state = viewModel.uiState.value
        assertFalse(state.signupSuccess)
        assertEquals("Email already exists", state.errorMessageSignup)
    }

    @Test
    fun `onSignupButtonClick with invalid form shows validation error and does not call use case`() = runTest {

        // GIVEN: Se crea un caso de uso simulado que no se llamará debido a errores de validación.
        viewModel = createViewModel()

        viewModel.onNameChange("A")
        viewModel.onLastnameChange("S")
        viewModel.onEmailChange("bad")
        viewModel.onPasswordChange("123")
        viewModel.onConfirmPasswordChange("321")

        // WHEN: Se llama a onSignupButtonClick.
        viewModel.onSignupButtonClick()
        advanceUntilIdle()

        // THEN: Se verifica que el estado de la UI contiene mensajes de error de validación y no se llama al caso de uso.
        val state = viewModel.uiState.value
        assertValidationErrors(state)
        coVerify(exactly = 0) { registerUserUseCase(any()) } // Verifica que el caso de uso no se haya llamado
    }


    @Test
    fun `onSignupButtonClick with exception shows error message`() = runTest {

        // GIVEN: Se crea un caso de uso simulado que lanza una excepción al registrar un usuario.
        coEvery { registerUserUseCase(any()) } throws RuntimeException("Network error")
        viewModel = SignupViewModel(sharedPreferences, registerUserUseCase, StandardTestDispatcher(testScheduler))

        givenfillValidForm(viewModel)

        // WHEN: Se llama a onSignupButtonClick.
        viewModel.onSignupButtonClick()
        advanceUntilIdle()

        // THEN: Se verifica que el estado de la UI se actualiza con el mensaje de error.
        val state = viewModel.uiState.value
        assertTrue(state.errorMessageSignup.contains("Network error"))
    }

    @Test
    fun `onEmailChange with invalid email sets validation error`() = runTest {
        // GIVEN: Se crea un viewmodel simulado con datos válidos.
        viewModel = createViewModel()

        // WHEN: Se llama a onEmailChange con un email inválido.
        viewModel.onEmailChange("invalid-email")

        // THEN: Se verifica que el estado de la UI contiene el mensaje de error de validación.
        val state = viewModel.uiState.value
        assertEquals("Email inválido", state.errorMessageEmail)
    }

    @Test
    fun `onPasswordChange with short password sets validation error`() = runTest {
        // GIVEN: Se crea un viewmodel simulado con datos válidos.
        viewModel = createViewModel()

        // WHEN: Se llama a onPasswordChange con una contraseña corta.
        viewModel.onPasswordChange("123")

        // THEN: Se verifica que el estado de la UI contiene el mensaje de error de validación.
        val state = viewModel.uiState.value
        assertEquals("La contraseña debe tener entre 8 y 12 caracteres", state.errorMessagePassword)
    }

    @Test
    fun `onConfirmPasswordChange with mismatch sets validation error`() = runTest {

        // GIVEN: Se crea un viewmodel y se establece una contraseña válida.
        viewModel = createViewModel()
        viewModel.onPasswordChange("password12")

        // WHEN: Se llama a onConfirmPasswordChange con una contraseña que no coincide.
        viewModel.onConfirmPasswordChange("different")

        // THEN: Se verifica que el estado de la UI contiene el mensaje de error de validación.
        val state = viewModel.uiState.value
        assertEquals("Las contraseñas no coinciden", state.errorMessagePasswordConfirm)
    }



    // FUNCIONES AUXILIARES

    private fun createViewModel() = SignupViewModel(sharedPreferences, registerUserUseCase, StandardTestDispatcher(testScheduler))


    private fun mockSharedPreferences(): SharedPreferences.Editor {
        val editor = mockk<SharedPreferences.Editor>(relaxed = true)
        every { sharedPreferences.edit() } returns editor
        every { editor.putString(any(), any()) } returns editor
        every { editor.apply() } just Runs
        return editor
    }

    private fun givenfillValidForm(
        viewModel: SignupViewModel,
    ){
        viewModel.onNameChange("Nombre")
        viewModel.onLastnameChange("Apellido")
        viewModel.onEmailChange("a@a.com")
        viewModel.onPasswordChange("password123")
        viewModel.onConfirmPasswordChange("password123")
    }

    private fun assertValidationErrors(state: SignupUiState) {
        assertEquals("El nombre no puede estar vacío", state.errorMessageName)
        assertEquals("El apellido no puede estar vacío", state.errorMessageLastname)
        assertEquals("Email inválido", state.errorMessageEmail)
        assertEquals("La contraseña debe tener entre 8 y 12 caracteres", state.errorMessagePassword)
        assertEquals("Las contraseñas no coinciden", state.errorMessagePasswordConfirm)
        assertEquals("Por favor, completa todos los campos correctamente.", state.errorMessageSignup)
    }

    private fun assertSignupSuccess(state: SignupUiState) {
        assertTrue(state.signupSuccess)
        assertEquals("", state.errorMessageSignup)
        assertEquals("", state.password)
        assertEquals("", state.passwordConfirm)
    }
}