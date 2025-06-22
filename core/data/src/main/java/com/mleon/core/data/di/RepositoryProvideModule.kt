package com.mleon.core.data.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryProvideModule {
    @Provides
    @Singleton
    fun provideProductRepositoryApi(apiService: com.mleon.core.data.remote.ApiService): com.mleon.core.data.repository.ProductRepositoryApi =
        com.mleon.core.data.repository.ProductRepositoryApi(apiService)

    @Provides
    @Singleton
    fun provideApiService(): com.mleon.core.data.remote.ApiService {
        return com.mleon.core.data.remote.RetrofitClient.instance
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences =
        appContext.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideUserRepository(apiService: com.mleon.core.data.remote.ApiService): com.mleon.core.data.repository.UserRepositoryApi =
        com.mleon.core.data.repository.UserRepositoryApi(apiService)
}
