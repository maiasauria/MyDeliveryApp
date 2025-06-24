package com.mleon.feature.profile.views

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.mleon.feature.profile.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel()) {
    val state = viewModel.uiState.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(state.errorMessage) {
        if (state.errorMessage.isNotEmpty()) {
            Toast.makeText(context, state.errorMessage, Toast.LENGTH_LONG).show()
        }
    }
    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            Toast.makeText(context, "Perfil guardado", Toast.LENGTH_LONG).show()
            viewModel.clearSavedFlag()
        }
    }

    ProfileView(
        onNameChange = viewModel::onNameChange,
        onLastnameChange = viewModel::onLastnameChange,
        onEmailChange = viewModel::onEmailChange,
        onAddressChange = viewModel::onAddressChange,
        onSave = viewModel::updateProfile,
        onImageUriChange = viewModel::onImageUriChange,
        name = state.name,
        lastname = state.lastname,
        email = state.email,
        address = state.address,
        userImageUrl = state.userImageUrl,
        isLoading = state.isLoading,

        isImageUploading = state.isImageUploading,
        isFormValid = state.isFormValid,
        errorMessageName = state.errorMessageName,
        errorMessageLastname = state.errorMessageLastname,
        errorMessageEmail = state.errorMessageEmail,
        errorMessageAddress = state.errorMessageAddress,
    )
}
