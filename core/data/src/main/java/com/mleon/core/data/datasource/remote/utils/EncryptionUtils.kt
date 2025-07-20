package com.mleon.core.data.datasource.remote.utils

import android.util.Base64
import com.mleon.core.data.BuildConfig
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object EncryptionUtils {
    fun encryptPassword(password: String): String {
        val secret = BuildConfig.API_ENCRYPTION_KEY
        val keySpec = SecretKeySpec(secret.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, keySpec)
        val encrypted = cipher.doFinal(password.toByteArray())
        return Base64.encodeToString(encrypted, Base64.DEFAULT)
    }

    fun decryptPassword(encryptedPassword: String): String {
        val secret = BuildConfig.API_ENCRYPTION_KEY
        val keySpec = SecretKeySpec(secret.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, keySpec)
        val decodedBytes = Base64.decode(encryptedPassword, Base64.DEFAULT)
        val decrypted = cipher.doFinal(decodedBytes)
        return String(decrypted)
    }
}