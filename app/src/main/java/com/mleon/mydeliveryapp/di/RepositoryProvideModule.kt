package com.mleon.mydeliveryapp.di

import android.content.Context
import android.content.SharedPreferences
import com.mleon.mydeliveryapp.data.repository.ProductRepositoryImpl
import com.mleon.mydeliveryapp.data.repository.UserRepositoryImpl
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
    fun provideProductRepositoryImpl(): ProductRepositoryImpl =
        ProductRepositoryImpl(/* dependencies */)

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences =
        appContext.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideUserRepository(): UserRepositoryImpl = UserRepositoryImpl()
}
