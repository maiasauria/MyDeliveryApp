package com.mleon.core.data.image

import com.cloudinary.Cloudinary
import com.mleon.core.data.repository.interfaces.ImageUploader
import java.io.InputStream
import javax.inject.Inject

class CloudinaryImageUploader @Inject constructor(
    private val cloudinary: Cloudinary
) : ImageUploader {
    override fun uploadImage(inputStream: InputStream, options: Map<String, Any>): String {
        val uploadResult = cloudinary.uploader().upload(inputStream, options)
        return uploadResult["secure_url"] as? String ?: throw IllegalStateException("Upload failed")
    }
}