package com.mleon.mydeliveryapp.di

import com.mleon.mydeliveryapp.data.repository.ProductRepository
import com.mleon.mydeliveryapp.data.repository.ProductRepositoryImpl
import com.mleon.mydeliveryapp.data.repository.UserRepository
import com.mleon.mydeliveryapp.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryBindModule {
    @Binds
    @Singleton
    abstract fun bindProductRepository(
        impl: ProductRepositoryImpl
    ): ProductRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository
}