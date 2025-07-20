package com.mleon.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

private const val IMAGE_FILE_PREFIX = "profile_photo_"
private const val IMAGE_FILE_SUFFIX = ".jpg"
private const val PROVIDER_SUFFIX = ".provider"

object UriUtils {
    fun createImageUri(context: Context): Uri? {
        val image = File(
            context.cacheDir,
            "$IMAGE_FILE_PREFIX${System.currentTimeMillis()}$IMAGE_FILE_SUFFIX"
        )
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}$PROVIDER_SUFFIX",
            image
        )
    }
}