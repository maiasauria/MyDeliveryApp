package com.mleon.core.data.repository.interfaces

import java.io.InputStream

interface ImageUploader {
    fun uploadImage(inputStream: InputStream, options: Map<String, Any>): String
}