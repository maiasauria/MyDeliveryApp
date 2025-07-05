package com.mleon.feature.profile.viewmodel

import android.app.Application
import android.util.Log
import com.mleon.core.model.User
import com.mleon.feature.profile.MainDispatcherRule
import com.mleon.feature.profile.usecase.GetUserProfileUseCase
import com.mleon.feature.profile.usecase.LogoutUserUseCase
import com.mleon.feature.profile.usecase.UpdateUserProfileUseCase
import com.mleon.feature.profile.usecase.UploadUserImageUseCase
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
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProfileViewModelTest {

 @get:Rule
 val mainDispatcherRule = MainDispatcherRule()

 private val getUserProfileUseCase: GetUserProfileUseCase = mockk()
 private val updateUserProfileUseCase: UpdateUserProfileUseCase = mockk()
 private val uploadUserImageUseCase: UploadUserImageUseCase = mockk()
 private val logoutUserUseCase: LogoutUserUseCase = mockk()

 private lateinit var viewModel: ProfileViewModel


 @Before
 fun setUp() {
  mockkStatic(Log::class)
  every { Log.e(any(), any()) } returns 0
 }

 @Test
 fun `uiState is Success when profile loads successfully`() = runTest {
  coEvery { getUserProfileUseCase() } returns mockUser()
  val application: Application = mockk(relaxed = true)
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
  val state = viewModel.uiState.value
  Assert.assertTrue("Expected Success, got $state", state is ProfileUiState.Success)
  Assert.assertEquals("Nombre", (state as ProfileUiState.Success).data.name)
 }

 @Test
 fun `uiState is Error when profile load fails`() = runTest {
  coEvery { getUserProfileUseCase() } throws RuntimeException("Failed to load")
  val application: Application = mockk(relaxed = true)
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
  val state = viewModel.uiState.value
  Assert.assertTrue("Expected Error, got $state", state is ProfileUiState.Error)
  Assert.assertTrue((state as ProfileUiState.Error).message.contains("Failed to load"))
 }

 @Test
 fun `uiState is Success and updated when updateProfile succeeds`() = runTest {
  coEvery { getUserProfileUseCase() } returns mockUser()
  coEvery { updateUserProfileUseCase(any()) } returns Unit
  val application: Application = mockk(relaxed = true)
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
  viewModel.onNameChange("Janeish")
  advanceUntilIdle()
  viewModel.updateProfile()
  advanceUntilIdle()
  val state = viewModel.uiState.value
  Assert.assertTrue("Expected Success, got $state", state is ProfileUiState.Success)
  Assert.assertEquals("Janeish", (state as ProfileUiState.Success).data.name)
 }

 @Test
 fun `uiState is Error when updateProfile fails`() = runTest {
  coEvery { getUserProfileUseCase() } returns mockUser()
  coEvery { updateUserProfileUseCase(any()) } throws RuntimeException("Update failed")
  val application: Application = mockk(relaxed = true)
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
  val state = viewModel.uiState.value
  Assert.assertTrue("Expected Error, got $state", state is ProfileUiState.Error)
  Assert.assertTrue((state as ProfileUiState.Error).message.contains("Update failed"))
 }

 // Helpers

 private fun mockUser(): User = User("a@a.com", "Nombre", "Apellido", "123 Main St", "imgUrl")
}