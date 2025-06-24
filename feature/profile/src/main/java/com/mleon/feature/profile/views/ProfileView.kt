package com.mleon.feature.profile.views

import android.R
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.mleon.utils.ui.ScreenTitle
import com.mleon.utils.ui.ValidateEmailField
import com.mleon.utils.ui.ValidateTextField

@Composable
fun ProfileView(
    name: String,
    lastname: String,
    email: String,
    address: String,
    userImageUrl: String = "",
    isLoading: Boolean = false,
    isImageUploading: Boolean = false,
    onNameChange: (String) -> Unit = {},
    onLastnameChange: (String) -> Unit = {},
    onEmailChange: (String) -> Unit = {},
    onAddressChange: (String) -> Unit = {},
    onImageUriChange: (Uri?) -> Unit = {},
    onSave: () -> Unit = {},
    errorMessageName: String = "",
    errorMessageLastname: String = "",
    errorMessageEmail: String = "",
    errorMessageAddress: String = "",
    isFormValid: Boolean = false,
) {
    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
        ) { uri: Uri? ->
            onImageUriChange(uri)
        }

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ScreenTitle("Perfil")

        UserImage(
            userImageUrl = userImageUrl,
            onClick = { launcher.launch("image/*") },
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        ValidateTextField(
            value = name,
            onValueChange = onNameChange,
            label = "Nombre",
            isError = errorMessageName.isNotEmpty(),
            errorMessage = errorMessageName,
            enabled = !isLoading,
        )

        ValidateTextField(
            value = lastname,
            onValueChange = onLastnameChange,
            label = "Apellido",
            isError = errorMessageLastname.isNotEmpty(),
            errorMessage = errorMessageLastname,
            enabled = !isLoading,
        )

        ValidateEmailField(
            value = email,
            onValueChange = onEmailChange,
            label = "Email",
            isError = errorMessageEmail.isNotEmpty(),
            errorMessage = errorMessageEmail,
            enabled = !isLoading,
        )

        ValidateTextField(
            value = address,
            onValueChange = onAddressChange,
            label = "Direccion",
            isError = errorMessageAddress.isNotEmpty(),
            errorMessage = errorMessageAddress,
            enabled = !isLoading,
        )

        Button(
            onClick = onSave,
            modifier = Modifier.align(Alignment.End),
            enabled = !isLoading && isFormValid,
        ) {
            Text("Guardar")
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        if (isImageUploading) {
            AlertDialog(
                onDismissRequest = { /* Do nothing */ },
                confirmButton = {},
                title = { Text("Cargando Imagen") },
                text = {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        CircularProgressIndicator()
                        Text("Por favor, espera mientras se carga la imagen.")
                    }
                },
            )
        }
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
                .size(128.dp)
                .clickable { onClick() },
    ) {
        if (userImageUrl.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(model = userImageUrl),
                contentDescription = "Profile Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.ic_menu_camera),
                contentDescription = "Add Image",
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(24.dp),
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
        isImageUploading = false,
    )
}
