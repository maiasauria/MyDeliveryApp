package com.mleon.core.domain.usecase.user

import android.app.Application
import android.net.Uri
import com.mleon.core.data.repository.interfaces.ImageUploader
import javax.inject.Inject

private const val UPLOAD_PRESET_KEY = "upload_preset"
private const val UPLOAD_PRESET_VALUE = "ProfileImage"
private const val ERROR_LOADING_IMAGE = "Error al cargar imagen de perfil"

class UploadUserImageUseCase @Inject constructor(
    private val imageUploader: ImageUploader,
    private val application: Application
) {
    operator fun invoke(uri: Uri): String {
        val inputStream = application.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("$ERROR_LOADING_IMAGE $uri")
        return imageUploader.uploadImage(
            inputStream,
            mapOf(UPLOAD_PRESET_KEY to UPLOAD_PRESET_VALUE)
        )
    }
}