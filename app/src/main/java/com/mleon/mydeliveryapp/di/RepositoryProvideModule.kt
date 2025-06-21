package com.mleon.mydeliveryapp.di

import android.content.Context
import android.content.SharedPreferences
import com.mleon.mydeliveryapp.data.remote.ApiService
import com.mleon.mydeliveryapp.data.remote.RetrofitClient
import com.mleon.mydeliveryapp.data.repository.ProductRepositoryApi
import com.mleon.mydeliveryapp.data.repository.UserRepositoryApi
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
    fun provideProductRepositoryApi(apiService: ApiService): ProductRepositoryApi =
        ProductRepositoryApi(apiService)

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return RetrofitClient.instance
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences =
        appContext.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideUserRepository(apiService: ApiService): UserRepositoryApi = UserRepositoryApi(apiService)
}
