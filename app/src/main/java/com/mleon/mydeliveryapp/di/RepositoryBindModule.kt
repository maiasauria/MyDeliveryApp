package com.mleon.mydeliveryapp.di

import com.mleon.mydeliveryapp.data.repository.ProductRepository
import com.mleon.mydeliveryapp.data.repository.ProductRepositoryApi
import com.mleon.mydeliveryapp.data.repository.UserRepository
import com.mleon.mydeliveryapp.data.repository.UserRepositoryApi
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
        impl: ProductRepositoryApi
    ): ProductRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryApi
    ): UserRepository
}