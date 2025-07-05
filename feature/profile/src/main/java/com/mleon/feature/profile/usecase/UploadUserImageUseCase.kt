package com.mleon.feature.profile.usecase

import android.app.Application
import android.net.Uri
import com.cloudinary.Cloudinary
import javax.inject.Inject

class UploadUserImageUseCase @Inject constructor(
    private val cloudinary: Cloudinary,
    private val application: Application
) {
    operator fun invoke(uri: Uri): String {
        val inputStream = application.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Unable to open input stream for URI: $uri")
        val result = cloudinary.uploader().upload(inputStream, mapOf("upload_preset" to "ProfileImage"))
        return result["secure_url"] as String
    }
}