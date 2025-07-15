package com.mleon.feature.profile.views


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.rememberAsyncImagePainter
import com.mleon.core.model.User
import com.mleon.feature.profile.R
import com.mleon.utils.ui.ScreenTitle
import com.mleon.utils.ui.ValidateEmailField
import com.mleon.utils.ui.ValidateTextField
import com.mleon.utils.ui.YappSmallLoadingIndicator


@Composable
fun ProfileView(
    name: String,
    lastname: String,
    email: String,
    address: String,
    userImageUrl: String = "",
    isLoading: Boolean = false,
    onNameChange: (String) -> Unit = {},
    onLastnameChange: (String) -> Unit = {},
    onEmailChange: (String) -> Unit = {},
    onAddressChange: (String) -> Unit = {},
    onLogoutRequest: () -> Unit = {},
    errorMessageName: String = "",
    errorMessageLastname: String = "",
    errorMessageEmail: String = "",
    errorMessageAddress: String = "",
    isFormValid: Boolean = false,
    onRequestCamera: () -> Unit = {},
    onRequestGallery: () -> Unit = {},
    onShowPreview: (User) -> Unit,
) {



    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.profile_screen_padding)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.profile_screen_spacing)),
        ) {
            ScreenTitle(stringResource(R.string.profile_title))

            UserImage(
                userImageUrl = userImageUrl,
                onClick = onRequestGallery, //al tocar la imagen, se abre el selector de imagen de la galeria
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = onRequestCamera, enabled = !isLoading) {
                    Text(stringResource(R.string.profile_take_photo))
                }
                Button(onClick = onRequestGallery, enabled = !isLoading) {
                    Text(stringResource(R.string.profile_upload_photo))
                }
            }
            ValidateTextField(
                value = name,
                onValueChange = onNameChange,
                label = stringResource(R.string.profile_name_label),
                isError = errorMessageName.isNotEmpty(),
                errorMessage = errorMessageName,
                enabled = !isLoading,
            )

            ValidateTextField(
                value = lastname,
                onValueChange = onLastnameChange,
                label = stringResource(R.string.profile_lastname_label),
                isError = errorMessageLastname.isNotEmpty(),
                errorMessage = errorMessageLastname,
                enabled = !isLoading,
            )

            ValidateEmailField(
                value = email,
                onValueChange = onEmailChange,
                label = stringResource(R.string.profile_email_label),
                isError = errorMessageEmail.isNotEmpty(),
                errorMessage = errorMessageEmail,
                enabled = !isLoading,
            )

            ValidateTextField(
                value = address,
                onValueChange = onAddressChange,
                label = stringResource(R.string.profile_address_label),
                isError = errorMessageAddress.isNotEmpty(),
                errorMessage = errorMessageAddress,
                enabled = !isLoading,
            )

            SaveProfileButton(
                isLoading = isLoading,
                isFormValid = isFormValid,
           //     onSave = onSave,
                onSave = {
                    val userDraft = User(
                        name = name,
                        lastname = lastname,
                        email = email,
                        address = address,
                        userImageUrl = userImageUrl
                    )
                    onShowPreview(userDraft)
                }
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.profile_spacer_16)))

            LogoutButton(
                onClick = onLogoutRequest
            )
        }
    }
}

@Composable
fun SaveProfileButton(
    isLoading: Boolean,
    isFormValid: Boolean,
    onSave: () -> Unit,
) {
    Button(
        onClick = onSave,
        modifier = Modifier.fillMaxWidth(),
        enabled = !isLoading && isFormValid,
    ) {
        Text(stringResource(R.string.profile_save))
    }
}

@Composable
fun LogoutButton(
    onClick: () -> Unit,
) {
    TextButton(
        onClick = { onClick() },
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text("Cerrar sesión")
    }
}

@Composable
fun UserImage(
    userImageUrl: String,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Card(
        modifier =
            modifier
                .size(dimensionResource(id = R.dimen.profile_user_image_size))
                .clickable { onClick() },
    ) {
        if (userImageUrl.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(model = userImageUrl),
                contentDescription = stringResource(R.string.profile_image_content_desc),

                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.ic_menu_camera),
                contentDescription = stringResource(R.string.profile_add_image_content_desc),
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(id = R.dimen.profile_user_icon_padding)),
            )
        }
    }
}

@Preview
@Composable
private fun ProfileViewPreview() {
    ProfileView(
        name = "Nombre",
        lastname = "Apellido",
        email = "",
        address = "Calle 324",
        userImageUrl = "",
        isLoading = false,
        onNameChange = {},
        onLastnameChange = {},
        onEmailChange = {},
        onAddressChange = {},
        onLogoutRequest = {},
        onRequestCamera = {},
        onRequestGallery = {},
        onShowPreview = { draft -> /* Handle preview */ },
    )
}

@Composable
fun ProfilePreviewDialog(
    userDraft: User,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Actualizar Perfil") },
        text = {
            Column {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    UserImage(
                        userImageUrl = userDraft.userImageUrl ?: "",
                        modifier = Modifier.size(dimensionResource(id = R.dimen.profile_preview_image_size))
                    )
                }
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.profile_spacer_8)))
                Text("Nombre: ${userDraft.name}")
                Text("Apellido: ${userDraft.lastname}")
                Text("Email: ${userDraft.email}")
                Text("Dirección: ${userDraft.address}")
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) { Text("Confirmar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
fun LogoutDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onConfirm) { Text("Confirmar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        },
        title = { Text("Cerrar sesión") },
        text = { Text("Estás seguro de querer cerrar sesión?") }
    )
}

@Composable
fun ImageUploadingDialog() {
    AlertDialog(
        onDismissRequest = { /* Do nothing */ },
        confirmButton = {},
        title = { Text(stringResource(R.string.profile_loading_image_title)) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(id = R.dimen.profile_uploading_dialog_vertical_padding)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.profile_uploading_dialog_spacing)),
            ) {
                YappSmallLoadingIndicator()
                Text(stringResource(R.string.profile_loading_image_text))
            }
        },
    )
}

@Composable
fun PermissionDialog(
    permissionToRequest: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Se necesita permiso") },
        text = {
            Text(
                if (permissionToRequest.contains("CAMERA"))
                    "Yapp necesita acceso a tu cámara para sacar fotos"
                else
                    "Yapp necesita acceso a tu cámara para elegir imágenes"
            )
        },
        confirmButton = {
            Button(onClick = onConfirm) { Text("Permitir") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Rechazar") }
        }
    )
}