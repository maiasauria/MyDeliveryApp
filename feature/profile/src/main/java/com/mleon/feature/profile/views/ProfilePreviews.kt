package com.mleon.feature.profile.views

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mleon.core.model.User
import com.mleon.feature.profile.viewmodel.ProfileFormState
import com.mleon.feature.profile.viewmodel.UserDataState


@Preview
@Composable
private fun ProfilePreviewDialogPreview() {
    ProfilePreviewDialog(
        userDraft = User(
            name = "Nombre",
            lastname = "Apellido",
            email = "asd@a.a",
            address = "Calle 324",
            userImageUrl = "https://example.com/image.jpg"
        ),
        onConfirm = { },
        onDismiss = { }
    )
}

@Preview(showBackground = true)
@Composable
private fun ProfileViewPreview() {
    ProfileView(
        userData = UserDataState(
            name = "Nombre",
            lastname = "Apellido",
            email = "asd@a.a",
            address = "Calle 324",
            userImageUrl = "https://example.com/image.jpg",
            userImageUri = Uri.EMPTY,
        ),
        formState = ProfileFormState(
            isUploading = false
        ),
        onShowPreview = { },
    )
}

@Preview(
    showBackground = true,
    widthDp = 640,
    heightDp = 360
)
@Composable
private fun ProfileViewPreviewLandscape() {
    ProfileViewPreview()
}