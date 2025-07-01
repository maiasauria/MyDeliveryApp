package com.mleon.core.data.di

import com.cloudinary.Cloudinary
import com.mleon.core.data.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CloudinaryModule {

    val apiKey = BuildConfig.CLOUDINARY_API_KEY
    val apiSecret = BuildConfig.CLOUDINARY_API_SECRET
    val cloudName = BuildConfig.CLOUDINARY_CLOUD_NAME

    private val cloudinary =
        Cloudinary(
            mapOf(
                "cloud_name" to cloudName,
                "api_key" to apiKey,
                "api_secret" to apiSecret,
            ),
        )

    @Provides
    @Singleton
    fun provideCloudinary(): Cloudinary {
        return cloudinary
    }
}