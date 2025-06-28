package com.mleon.feature.profile.views

import android.Manifest
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.mleon.feature.profile.viewmodel.ProfileViewModel
import java.io.File

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel()) {
    val state = viewModel.uiState.collectAsState().value
    val context = LocalContext.current

    // Permission dialog state
    var showPermissionDialog by remember { mutableStateOf(false) }
    var permissionToRequest by remember { mutableStateOf("") }
    var pendingAction by remember { mutableStateOf<(() -> Unit)?>(null) }

    // For camera photo URI
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
        }
    )

    // AlertDialog before requesting permission
    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { Text("Permission Required") },
            text = {
                Text(
                    if (permissionToRequest == Manifest.permission.CAMERA)
                        "This app needs camera access to take photos."
                    else
                        "This app needs access to your gallery to select images."
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showPermissionDialog = false
                    permissionLauncher.launch(permissionToRequest)
                }) { Text("Allow") }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionDialog = false }) { Text("Deny") }
            }
        )
    }
}