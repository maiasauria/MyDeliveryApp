package com.mleon.feature.profile.views

import android.Manifest
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mleon.core.model.User
import com.mleon.core.navigation.NavigationRoutes
import com.mleon.feature.profile.R
import com.mleon.feature.profile.viewmodel.ProfileUiState
import com.mleon.feature.profile.viewmodel.ProfileViewModel
import com.mleon.utils.UriUtils
import com.mleon.utils.ui.ErrorScreen
import com.mleon.utils.ui.YappFullScreenLoadingIndicator


const val IMAGE_ROUTE = "image/*"
const val CAMERA_PERMISSION = Manifest.permission.CAMERA

@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsState().value
    val context = LocalContext.current

    // Permission dialog state
    var permissionToRequest by rememberSaveable { mutableStateOf("") }
    var pendingAction by remember { mutableStateOf<(() -> Unit)?>(null) }
    var showLogoutDialog by rememberSaveable { mutableStateOf(false) }
    var showPreview by rememberSaveable { mutableStateOf(false) }
    var userDraft by remember { mutableStateOf<User?>(null) }

    // Estado para la imagen de la cámara
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

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            pendingAction?.invoke()
        } else {
            Toast.makeText(context, context.getString(R.string.profile_permission_denied), Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    when (uiState) {
        is ProfileUiState.Loading -> {
            YappFullScreenLoadingIndicator()
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
            val userData = uiState.userData

            LaunchedEffect(state.isSaved) {
                if (state.isSaved) {
                    Toast.makeText(context, context.getString(R.string.profile_saved_toast), Toast.LENGTH_LONG).show()
                    viewModel.clearSavedFlag()
                }
            }

            ProfileView(
                onNameChange = viewModel::onNameChange,
                onLastnameChange = viewModel::onLastnameChange,
                onEmailChange = viewModel::onEmailChange,
                onAddressChange = viewModel::onAddressChange,
                formState = state,
                userData = userData,
                onRequestCamera = {
                    permissionToRequest = CAMERA_PERMISSION
                    pendingAction = {
                        cameraImageUri = UriUtils.createImageUri(context)
                        cameraImageUri?.let { cameraLauncher.launch(it) }
                    }
                    //No chequeo SDK porque la app requiere 24 y el permiso está disponible desde 23
                    permissionLauncher.launch(permissionToRequest)
                },
                onRequestGallery = {
                    galleryLauncher.launch(IMAGE_ROUTE)
                },
                onLogoutRequest = { showLogoutDialog = true },
                onShowPreview = { draft ->
                    userDraft = draft
                    showPreview = true
                }
            )
        }
    }

    // Muestra el diálogo de previsualización del perfil si hay datos modificados
    if (showPreview && userDraft != null) {
        ProfilePreviewDialog(
            userDraft = userDraft!!,
            onConfirm = {
                viewModel.updateProfile()
                showPreview = false
            },
            onDismiss = {
                showPreview = false
                viewModel.loadProfile()
            },
        )
    }

    if (uiState is ProfileUiState.Success && uiState.data.isUploading) {
        UploadingDialog()
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
