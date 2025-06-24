package com.mleon.feature.profile.views

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.mleon.utils.ui.ScreenTitle

@Composable
fun ProfileView(
    name: String,
    lastname: String,
    email: String,
    address: String,
    userImageUrl: String?,
    isLoading: Boolean,
    isSaved: Boolean,
    errorMessage: String?,
    onNameChange: (String) -> Unit,
    onLastnameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onUserImageUrlChange: (String?) -> Unit,
    onImageUriChange: (android.net.Uri?) -> Unit,
    onSave: () -> Unit,
    onClearSaved: () -> Unit
) {
    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
        ) { uri: android.net.Uri? ->
            onImageUriChange(uri)
        }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ScreenTitle("Perfil")

        Card(
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterHorizontally),
        ) {
            if (!userImageUrl.isNullOrEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(model = userImageUrl),
                    contentDescription = "Profile Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_camera),
                    contentDescription = "Add Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                )
            }
        }
        Button(
            onClick = { launcher.launch("image/*") },
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            Text("Cargar Imagen")
        }
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = lastname,
            onValueChange = onLastnameChange,
            label = { Text("Apellido") },
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = address,
            onValueChange = onAddressChange,
            label = { Text("Dirección") },
            modifier = Modifier.fillMaxWidth(),
        )
        Button(
            onClick = onSave,
            modifier = Modifier.align(Alignment.End),
            enabled = !isLoading
        ) {
            Text("Guardar")
        }
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        errorMessage?.let {
            Snackbar { Text(it) }
        }
        if (isSaved) {
            Snackbar {
                Text("Perfil guardado")
                LaunchedEffect(Unit) { onClearSaved() }
            }
        }
    }
}