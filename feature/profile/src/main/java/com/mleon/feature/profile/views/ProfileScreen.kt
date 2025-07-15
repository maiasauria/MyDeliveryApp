package com.mleon.feature.profile.views

import android.Manifest
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mleon.core.model.User
import com.mleon.core.navigation.NavigationRoutes
import com.mleon.feature.profile.viewmodel.ProfileUiState
import com.mleon.feature.profile.viewmodel.ProfileViewModel
import com.mleon.utils.ui.ErrorScreen
import com.mleon.utils.ui.YappLoadingIndicator
import java.io.File


@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsState().value
    val context = LocalContext.current

    // Permission dialog state
    var showPermissionDialog by rememberSaveable { mutableStateOf(false) }
    var permissionToRequest by rememberSaveable { mutableStateOf("") }
    var pendingAction by remember { mutableStateOf<(() -> Unit)?>(null) }
    var showLogoutDialog by rememberSaveable { mutableStateOf(false) }
    var showPreview by rememberSaveable { mutableStateOf(false) }
    var userDraft by remember { mutableStateOf<User?>(null) }
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }


    // Gallery launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.onImageUriChange(uri)
    }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            viewModel.onImageUriChange(cameraImageUri)
        }
    }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            pendingAction?.invoke()
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    fun createImageUri(context: Context): Uri? {
        val image = File(context.cacheDir, "profile_photo_${System.currentTimeMillis()}.jpg")
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            image
        )
    }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    when (uiState) {
        is ProfileUiState.Loading -> {
            YappLoadingIndicator()
        }

        is ProfileUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ErrorScreen(
                    errorMessage = uiState.message,
                    onRetry = { viewModel.loadProfile() }
                )
            }
        }

        is ProfileUiState.Success -> {
            val state = uiState.data

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
                name = state.name,
                lastname = state.lastname,
                email = state.email,
                address = state.address,
                userImageUrl = state.userImageUrl,
                isLoading = state.isLoading,
                isFormValid = state.isFormValid,
                errorMessageName = state.errorMessageName,
                errorMessageLastname = state.errorMessageLastname,
                errorMessageEmail = state.errorMessageEmail,
                errorMessageAddress = state.errorMessageAddress,
                onRequestCamera = {
                    permissionToRequest = Manifest.permission.CAMERA
                    showPermissionDialog = true
                    pendingAction = {
                        cameraImageUri = createImageUri(context)
                        cameraImageUri?.let { cameraLauncher.launch(it) }
                    }
                },
                onRequestGallery = {
                    galleryLauncher.launch("image/*")
                },
                onLogoutRequest = { showLogoutDialog = true },
                onShowPreview = { draft ->
                    userDraft = draft
                    showPreview = true
                }
            )
        }
    }

    // Muestra el diálogo de permisos si es necesario
    if (showPermissionDialog) {
        PermissionDialog(
            permissionToRequest = permissionToRequest,
            onConfirm = {
                showPermissionDialog = false
                permissionLauncher.launch(permissionToRequest)
            },
            onDismiss = { showPermissionDialog = false }
        )
    }

    // Muestra el diálogo de previsualización del perfil si hay datos modificados
    if (showPreview && userDraft != null) {
        ProfilePreviewDialog(
            userDraft = userDraft!!,
            onConfirm = {
                viewModel.updateProfile()
                showPreview = false
            },
            onDismiss = { showPreview = false }
        )
    }

    if (uiState is ProfileUiState.Success && uiState.data.isImageUploading) {
        ImageUploadingDialog()
    }

    if (showLogoutDialog) {
        LogoutDialog(
            onConfirm = {
                showLogoutDialog = false
                viewModel.onLogoutClick()
                navController.navigate(NavigationRoutes.LOGIN) {
                    popUpTo(0) { inclusive = true }
                }
            },
            onDismiss = { showLogoutDialog = false }
        )
    }
}
