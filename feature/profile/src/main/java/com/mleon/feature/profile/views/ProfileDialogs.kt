package com.mleon.feature.profile.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.mleon.core.model.User
import com.mleon.feature.profile.R
import com.mleon.utils.ui.YappSmallLoadingIndicator


@Composable
fun ProfilePreviewDialog(
    userDraft: User,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.profile_preview_dialog_title)) },
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
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.profile_spacer)))
                Text(stringResource(R.string.profile_preview_name, userDraft.name))
                Text(stringResource(R.string.profile_preview_lastname, userDraft.lastname))
                Text(stringResource(R.string.profile_preview_email, userDraft.email))
                Text(stringResource(R.string.profile_preview_address, userDraft.address ?: ""))
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) { Text(stringResource(R.string.profile_confirm)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.profile_cancel)) }
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
            Button(onClick = onConfirm) { Text(stringResource(R.string.profile_confirm)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.profile_cancel)) }
        },
        title = { Text(stringResource(R.string.profile_logout_title)) },
        text = { Text(stringResource(R.string.profile_logout_message)) }
    )
}

@Composable
fun UploadingDialog() {
    AlertDialog(
        onDismissRequest = { /* No hacer nada */ },
        confirmButton = { },
        title = { Text(stringResource(R.string.profile_uploading_title)) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(
                        id = R.dimen.profile_uploading_dialog_vertical_padding)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    dimensionResource(id = R.dimen.profile_uploading_dialog_spacing)),
            ) {
                YappSmallLoadingIndicator()
                Text(stringResource(R.string.profile_uploading_text))
            }
        },
    )
}
