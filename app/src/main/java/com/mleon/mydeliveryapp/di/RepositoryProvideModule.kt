package com.mleon.mydeliveryapp.di

import com.mleon.mydeliveryapp.data.repository.ProductRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryProvideModule {
    @Provides
    @Singleton
    fun provideProductRepositoryImpl(): ProductRepositoryImpl = ProductRepositoryImpl(/* dependencies */)
}