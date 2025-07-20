package com.mleon.core.data.di

import android.content.Context
import com.mleon.core.data.BuildConfig
import com.mleon.core.data.datasource.remote.service.OrderApiService
import com.mleon.core.data.datasource.remote.service.ProductsApiService
import com.mleon.core.data.datasource.remote.service.UsersApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val HTTP_CACHE_SIZE_BYTES = 10 * 1024 * 1024 // 10MB
private const val HTTP_TIMEOUT_SECONDS = 60L

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG) {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            })
            .retryOnConnectionFailure(true)
            .cache(Cache(File(context.cacheDir, "http_cache"), HTTP_CACHE_SIZE_BYTES.toLong()))
            .connectTimeout(HTTP_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(HTTP_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(HTTP_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideProductsApiService(retrofit: Retrofit): ProductsApiService =
        retrofit.create(ProductsApiService::class.java)

    @Provides
    @Singleton
    fun provideUsersApiService(retrofit: Retrofit): UsersApiService =
        retrofit.create(UsersApiService::class.java)

    @Provides
    @Singleton
    fun provideOrderApiService(retrofit: Retrofit): OrderApiService =
        retrofit.create(OrderApiService::class.java)
}