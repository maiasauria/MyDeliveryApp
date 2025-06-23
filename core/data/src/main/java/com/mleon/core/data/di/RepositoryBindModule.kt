package com.mleon.core.data.di

import com.mleon.core.data.repository.OrdersRepository
import com.mleon.core.data.repository.OrdersRepositoryApi
import com.mleon.core.data.repository.ProductRepository
import com.mleon.core.data.repository.ProductRepositoryApi
import com.mleon.core.data.repository.UserRepository
import com.mleon.core.data.repository.UserRepositoryApi
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
    abstract fun bindProductRepository(impl: ProductRepositoryApi): ProductRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryApi): UserRepository

    @Binds
    @Singleton
    abstract fun bindOrdersRepository(impl: OrdersRepositoryApi): OrdersRepository
}
