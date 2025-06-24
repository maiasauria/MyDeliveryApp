package com.mleon.feature.profile.views

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.mleon.feature.profile.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsState().value
    ProfileView(
        onNameChange = viewModel::onNameChange,
        onLastnameChange = viewModel::onLastnameChange,
        onEmailChange = viewModel::onEmailChange,
        onAddressChange = viewModel::onAddressChange,
        onUserImageUrlChange = viewModel::onUserImageUrlChange,
        onSave = viewModel::updateProfile,
        onImageUriChange = {},
        onClearSaved = viewModel::clearSavedFlag,
        name = state.name,
        lastname = state.lastname,
        email = state.email,
        address = state.address,
        userImageUrl = state.userImageUrl,
        isLoading = state.isLoading,
        isSaved = state.isSaved,
        errorMessage = state.errorMessage,


    )
}