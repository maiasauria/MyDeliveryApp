package com.mleon.feature.profile.views


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil3.compose.rememberAsyncImagePainter
import com.mleon.core.model.User
import com.mleon.feature.profile.R
import com.mleon.feature.profile.viewmodel.ProfileFormState
import com.mleon.feature.profile.viewmodel.UserDataState
import com.mleon.utils.ui.ScreenTitle
import com.mleon.utils.ui.ValidateEmailField
import com.mleon.utils.ui.ValidateTextField


@Composable
fun ProfileView(
    userData : UserDataState,
    formState: ProfileFormState,
    onNameChange: (String) -> Unit = {},
    onLastnameChange: (String) -> Unit = {},
    onEmailChange: (String) -> Unit = {},
    onAddressChange: (String) -> Unit = {},
    onLogoutRequest: () -> Unit = {},
    onRequestCamera: () -> Unit = {},
    onRequestGallery: () -> Unit = {},
    onShowPreview: (User) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.profile_screen_spacing)),
    ) {
        ScreenTitle(stringResource(R.string.profile_title))

        ProfileImageSection(
            userImageUrl = userData.userImageUrl,
            onRequestCamera = onRequestCamera,
            onRequestGallery = onRequestGallery,
        )

        UserDataSection(
            userData = userData,
            onNameChange = onNameChange,
            onLastnameChange = onLastnameChange,
            onEmailChange = onEmailChange,
            onAddressChange = onAddressChange,
        )

        SaveProfileButton(
            isLoading = formState.isUploading,
            isFormValid = formState.isFormValid,
            isImageChanged = formState.isImageChanged,
            userData = userData,
            onShowPreview = onShowPreview,
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.profile_spacer)))

        LogoutButton(onClick = onLogoutRequest)
    }
}

@Composable
fun SaveProfileButton(
    isLoading: Boolean,
    isFormValid: Boolean,
    isImageChanged: Boolean = false,
    onShowPreview: (User) -> Unit,
    userData: UserDataState,
) {
    Button(
        onClick = {
            val userDraft = User(
                name = userData.name,
                lastname = userData.lastname,
                email = userData.email,
                address = userData.address,
                userImageUrl = userData.userImageUrl
            )
            onShowPreview(userDraft)
        },
        modifier = Modifier.fillMaxWidth(),
        enabled = !isLoading && (isFormValid || isImageChanged),
    ) {
        Text(stringResource(R.string.profile_save))
    }
}

@Composable
fun ProfileImageSection(
    userImageUrl: String,
    onRequestCamera: () -> Unit,
    onRequestGallery: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        UserImage(userImageUrl)
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = onRequestCamera) {
            Text(stringResource(R.string.profile_take_photo))
        }
        Button(onClick = onRequestGallery) {
            Text(stringResource(R.string.profile_upload_photo))
        }
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
        Text(stringResource(R.string.logout_button_text))
    }
}

@Composable
fun UserImage(
    userImageUrl: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier =
            modifier
                .size(dimensionResource(id = R.dimen.profile_user_image_size)),
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

@Composable
fun UserDataSection(
    userData: UserDataState,
    onNameChange: (String) -> Unit,
    onLastnameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
){
    ValidateTextField(
        value = userData.name,
        onValueChange = onNameChange,
        label = stringResource(R.string.profile_name_label),
        isError = userData.errorMessageName.isNotEmpty(),
        errorMessage = userData.errorMessageName,
    )

    ValidateTextField(
        value = userData.lastname,
        onValueChange = onLastnameChange,
        label = stringResource(R.string.profile_lastname_label),
        isError = userData.errorMessageLastname.isNotEmpty(),
        errorMessage = userData.errorMessageLastname,
    )

    ValidateEmailField(
        value = userData.email,
        onValueChange = onEmailChange,
        label = stringResource(R.string.profile_email_label),
        isError = userData.errorMessageEmail.isNotEmpty(),
        errorMessage = userData.errorMessageEmail,
    )

    ValidateTextField(
        value = userData.address,
        onValueChange = onAddressChange,
        label = stringResource(R.string.profile_address_label),
        isError = userData.errorMessageAddress.isNotEmpty(),
        errorMessage = userData.errorMessageAddress,
    )
}



