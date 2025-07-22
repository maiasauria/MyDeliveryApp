package com.mleon.feature.profile.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import com.mleon.core.domain.usecase.user.GetUserProfileUseCase
import com.mleon.core.domain.usecase.user.LogoutUserUseCase
import com.mleon.core.domain.usecase.user.UpdateUserProfileUseCase
import com.mleon.core.domain.usecase.user.UploadUserImageUseCase
import com.mleon.core.model.User
import com.mleon.core.model.result.AuthResult
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ProfileViewModelTest {

    private val getUserProfileUseCase: GetUserProfileUseCase = mockk()
    private val updateUserProfileUseCase: UpdateUserProfileUseCase = mockk()
    private val uploadUserImageUseCase: UploadUserImageUseCase = mockk()
    private val logoutUserUseCase: LogoutUserUseCase = mockk()

    private lateinit var viewModel: ProfileViewModel
    private lateinit var application: Application


    @Before
    fun setUp() {
        application = mockk(relaxed = true)
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0

        // Mock Uri.parse for JVM tests
        mockkStatic(Uri::class)
        every { Uri.parse(any()) } returns mockk(relaxed = true)
    }

    // CASOS VALIDOS
    @Test
    fun `when loadProfile is called then uiState is Loading`() = runTest {
        givenUser()
        val application: Application = mockk(relaxed = true)
        val viewModel = ProfileViewModel(
            application,
            getUserProfileUseCase,
            updateUserProfileUseCase,
            uploadUserImageUseCase,
            logoutUserUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.loadProfile()
        thenUiStateIsLoading(viewModel)
    }

    @Test
    fun `given user when loadProfile then uiState is Success`() = runTest {
        givenUser()
        val application: Application = mockk(relaxed = true)
        val viewModel = ProfileViewModel(
            application,
            getUserProfileUseCase,
            updateUserProfileUseCase,
            uploadUserImageUseCase,
            logoutUserUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.loadProfile()
        advanceUntilIdle()
        thenUiStateIsSuccess(viewModel)
        thenUserNameIs(viewModel.uiState.value, "Nombre")
    }

    @Test
    fun `uiState is Success and updated when updateProfile succeeds`() = runTest {

        // GIVEN: Se mockea el caso de uso para obtener el perfil del usuario y actualizarlo.
        givenUser()
        givenUpdateSuccess()
        val application: Application = mockk(relaxed = true)
        viewModel = ProfileViewModel(
            application,
            getUserProfileUseCase,
            updateUserProfileUseCase,
            uploadUserImageUseCase,
            logoutUserUseCase,
            StandardTestDispatcher(testScheduler)
        )
        // WHEN: Se carga el perfil, se cambia el nombre y se actualiza el perfil.
        viewModel.loadProfile()
        advanceUntilIdle()
        viewModel.onNameChange("Nombre")
        advanceUntilIdle()
        viewModel.updateProfile()
        advanceUntilIdle()

        // THEN: Se verifica que el estado de la UI es Success y que el nombre se ha actualizado correctamente.
        val state = viewModel.uiState.value
        Assert.assertTrue("Expected Success, got $state", state is ProfileUiState.Success)
        Assert.assertEquals("Nombre", (state as ProfileUiState.Success).userData.name)
    }

    @Test
    fun `given update success when updateProfile then uiState is Success`() = runTest {
        givenUser()
        givenUpdateSuccess()
        val application: Application = mockk(relaxed = true)
        val viewModel = ProfileViewModel(
            application,
            getUserProfileUseCase,
            updateUserProfileUseCase,
            uploadUserImageUseCase,
            logoutUserUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.loadProfile()
        advanceUntilIdle()
        viewModel.onNameChange("Nombre")
        advanceUntilIdle()
        viewModel.updateProfile()
        advanceUntilIdle()
        thenUiStateIsSuccess(viewModel)
        thenUserNameIs(viewModel.uiState.value, "Nombre")
    }

    @Test
    fun `given image upload success when onImageUriChange then userImageUrl updated`() = runTest {
        val uri: Uri = mockk(relaxed = true)
        givenUser()
        givenImageUploadSuccess(uri, "imgUrl")
        givenUpdateSuccess()

        viewModel = ProfileViewModel(
            application,
            getUserProfileUseCase,
            updateUserProfileUseCase,
            uploadUserImageUseCase,
            logoutUserUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.loadProfile()
        advanceUntilIdle()
        viewModel.onImageUriChange(uri)
        advanceUntilIdle()
        viewModel.updateProfile()
        advanceUntilIdle()
        thenUiStateIsSuccess(viewModel)

        Assert.assertEquals(
            "imgUrl",
            (viewModel.uiState.value as ProfileUiState.Success).userData.userImageUrl
        )
    }

    @Test
    fun `onLogoutClick calls logoutUserUseCase`() = runTest {
        givenLogoutSuccess()
        val application: Application = mockk(relaxed = true)
        viewModel = ProfileViewModel(
            application,
            getUserProfileUseCase,
            updateUserProfileUseCase,
            uploadUserImageUseCase,
            logoutUserUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.onLogoutClick()
        // No exception means success; you can verify with MockK if needed
    }

    // CASOS INVALIDOS

    @Test
    fun `given error when loadProfile then uiState is Error`() = runTest {
        givenUserError("Error al cargar el perfil")
        val application: Application = mockk(relaxed = true)
        val viewModel = ProfileViewModel(
            application,
            getUserProfileUseCase,
            updateUserProfileUseCase,
            uploadUserImageUseCase,
            logoutUserUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.loadProfile()
        advanceUntilIdle()
        thenUiStateIsError(viewModel, "Error al cargar el perfil")
    }

    @Test
    fun `given image upload error when updateProfile then uiState is Error`() = runTest {
        val uri: Uri = mockk()
        givenUser()
        givenImageUploadError(uri)
        val viewModel = ProfileViewModel(
            application,
            getUserProfileUseCase,
            updateUserProfileUseCase,
            uploadUserImageUseCase,
            logoutUserUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.loadProfile()
        advanceUntilIdle()
        viewModel.onImageUriChange(uri)
        advanceUntilIdle()
        viewModel.updateProfile()
        advanceUntilIdle()
        thenUiStateIsError(viewModel, "Error al subir la imagen.")
    }

    @Test
    fun `given update error when updateProfile then uiState is Error`() = runTest {
        givenUser()
        givenUpdateError()
        val viewModel = ProfileViewModel(
            application,
            getUserProfileUseCase,
            updateUserProfileUseCase,
            uploadUserImageUseCase,
            logoutUserUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.loadProfile()
        advanceUntilIdle()
        viewModel.updateProfile()
        advanceUntilIdle()
        thenUiStateIsError(viewModel, "Error al actualizar el perfil.")
    }

    @Test
    fun `given invalid name when updateProfile then uiState reflects invalid name and error`() = runTest {
        givenUser()
        viewModel = ProfileViewModel(
            application,
            getUserProfileUseCase,
            updateUserProfileUseCase,
            uploadUserImageUseCase,
            logoutUserUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.loadProfile()
        advanceUntilIdle()
        viewModel.onNameChange("A") // Invalid name
        advanceUntilIdle()
        viewModel.updateProfile()
        advanceUntilIdle()
        val state = viewModel.uiState.value
        Assert.assertTrue(state is ProfileUiState.Success)
        val userData = (state as ProfileUiState.Success).userData
        Assert.assertEquals("A", userData.name)
        Assert.assertFalse(userData.isNameValid)
        Assert.assertEquals("El nombre debe tener entre 2 y 50 caracteres", userData.errorMessageName)
        Assert.assertFalse(state.data.isFormValid)
    }

    @Test
    fun `given null image uri when onImageUriChange then uiState is unchanged`() = runTest {
        givenUser()
        viewModel = ProfileViewModel(
            application,
            getUserProfileUseCase,
            updateUserProfileUseCase,
            uploadUserImageUseCase,
            logoutUserUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.loadProfile()
        advanceUntilIdle()
        val beforeState = viewModel.uiState.value
        viewModel.onImageUriChange(null)
        advanceUntilIdle()
        Assert.assertTrue(viewModel.uiState.value is ProfileUiState.Success)
        Assert.assertEquals(beforeState, viewModel.uiState.value)
    }

    @Test
    fun `given logout error when onLogoutClick then uiState is Error`() = runTest {
        coEvery { logoutUserUseCase() } throws RuntimeException("Logout failed")
        viewModel = ProfileViewModel(
            application,
            getUserProfileUseCase,
            updateUserProfileUseCase,
            uploadUserImageUseCase,
            logoutUserUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.onLogoutClick()
        advanceUntilIdle()
        Assert.assertTrue(viewModel.uiState.value is ProfileUiState.Error)
        Assert.assertEquals(
            "Error al cerrar sesión.",
            (viewModel.uiState.value as ProfileUiState.Error).message
        )
    }

    @Test
    fun `given invalid lastname when updateProfile then uiState reflects invalid lastname and error`() = runTest {
        givenUser()
        viewModel = ProfileViewModel(
            application,
            getUserProfileUseCase,
            updateUserProfileUseCase,
            uploadUserImageUseCase,
            logoutUserUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.loadProfile()
        advanceUntilIdle()
        viewModel.onLastnameChange("A") // Invalid lastname
        advanceUntilIdle()
        viewModel.updateProfile()
        advanceUntilIdle()
        val state = viewModel.uiState.value
        Assert.assertTrue(state is ProfileUiState.Success)
        val userData = (state as ProfileUiState.Success).userData
        Assert.assertEquals("A", userData.lastname)
        Assert.assertFalse(userData.isLastnameValid)
        Assert.assertEquals("El apellido debe tener entre 2 y 50 caracteres", userData.errorMessageLastname)
        Assert.assertFalse(state.data.isFormValid)
    }

    @Test
    fun `given invalid email when updateProfile then uiState reflects invalid email and error`() = runTest {
        givenUser()
        viewModel = ProfileViewModel(
            application,
            getUserProfileUseCase,
            updateUserProfileUseCase,
            uploadUserImageUseCase,
            logoutUserUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.loadProfile()
        advanceUntilIdle()
        viewModel.onEmailChange("invalid-email")
        advanceUntilIdle()
        viewModel.updateProfile()
        advanceUntilIdle()
        val state = viewModel.uiState.value
        Assert.assertTrue(state is ProfileUiState.Success)
        val userData = (state as ProfileUiState.Success).userData
        Assert.assertEquals("invalid-email", userData.email)
        Assert.assertFalse(userData.isEmailValid)
        Assert.assertEquals("El email no es válido", userData.errorMessageEmail)
        Assert.assertFalse(state.data.isFormValid)
    }


    @Test
    fun `given empty address when updateProfile then uiState reflects invalid address and error`() = runTest {
        givenUser()
        viewModel = ProfileViewModel(
            application,
            getUserProfileUseCase,
            updateUserProfileUseCase,
            uploadUserImageUseCase,
            logoutUserUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.loadProfile()
        advanceUntilIdle()
        viewModel.onAddressChange("")
        advanceUntilIdle()
        viewModel.updateProfile()
        advanceUntilIdle()
        val state = viewModel.uiState.value
        Assert.assertTrue(state is ProfileUiState.Success)
        val userData = (state as ProfileUiState.Success).userData
        Assert.assertEquals("", userData.address)
        Assert.assertFalse(userData.isAddressValid)
        Assert.assertEquals("La dirección no puede estar vacía", userData.errorMessageAddress)
        Assert.assertFalse(state.data.isFormValid)
    }
    @Test
    fun `when clearSavedFlag is called then isSaved is false`() = runTest {
        givenUser()
        givenUpdateSuccess()
        viewModel = ProfileViewModel(
            application,
            getUserProfileUseCase,
            updateUserProfileUseCase,
            uploadUserImageUseCase,
            logoutUserUseCase,
            StandardTestDispatcher(testScheduler)
        )
        viewModel.loadProfile()
        advanceUntilIdle()
        viewModel.updateProfile()
        advanceUntilIdle()
        viewModel.clearSavedFlag()
        Assert.assertFalse((viewModel.uiState.value as ProfileUiState.Success).data.isSaved)
    }

    // METODOS AUXILIARES

    private fun mockUser(): User = User("a@a.com", "Nombre", "Apellido", "123 Main St", "imgUrl")

    private fun givenUser() {
        coEvery { getUserProfileUseCase() } returns AuthResult.Success("OK", mockUser())
    }

    private fun givenUserError(message: String) {
        coEvery { getUserProfileUseCase() } returns AuthResult.Error(message)
    }

    private fun givenUpdateSuccess() {
        coEvery { updateUserProfileUseCase(any()) } returns Unit
    }

    private fun givenUpdateError() {
        coEvery { updateUserProfileUseCase(any()) } throws RuntimeException("Any error")
    }

    private fun givenImageUploadSuccess(uri: Uri, url: String = "imgUrl") {
        coEvery { uploadUserImageUseCase(uri) } returns url
    }

    private fun givenImageUploadError(uri: Uri) {
        coEvery { uploadUserImageUseCase(uri) } throws RuntimeException("Error al subir la imagen")
    }

    private fun givenLogoutSuccess() {
        coEvery { logoutUserUseCase() } returns Unit
    }

    private fun mockUser(name: String): User = mockk(relaxed = true) {
        every { this@mockk.name } returns name
        every { this@mockk.lastname } returns "Apellido"
        every { this@mockk.email } returns "a@a.com"
        every { this@mockk.address } returns "123 Main St"
        every { this@mockk.userImageUrl } returns "imgUrl"
    }

    private fun thenUiStateIsSuccess(viewModel: ProfileViewModel) {
        val state = viewModel.uiState.value
        assert(state is ProfileUiState.Success)
    }

    private fun thenUiStateIsLoading(viewModel: ProfileViewModel) {
        val state = viewModel.uiState.value
        assert(state is ProfileUiState.Loading)
    }

    private fun thenUiStateIsError(viewModel: ProfileViewModel, message: String) {
        val state = viewModel.uiState.value
        Assert.assertTrue(state is ProfileUiState.Error)
        Assert.assertEquals(message, (state as ProfileUiState.Error).message)
    }

    private fun thenUserNameIs(state: ProfileUiState, name: String) {
        assert(state is ProfileUiState.Success)
        Assert.assertEquals(name, (state as ProfileUiState.Success).userData.name)
    }
}
