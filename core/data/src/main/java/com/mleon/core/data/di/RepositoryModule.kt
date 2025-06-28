package com.mleon.core.data.di

import com.mleon.core.data.datasource.ProductDataSource
import com.mleon.core.data.datasource.remote.OrderRemoteDataSource
import com.mleon.core.data.datasource.remote.ProductRemoteDataSource
import com.mleon.core.data.datasource.remote.UserRemoteDataSource
import com.mleon.core.data.remote.OrderApiService
import com.mleon.core.data.remote.ProductsApiService
import com.mleon.core.data.remote.UsersApiService
import com.mleon.core.data.repository.CartItemRepositoryImpl
import com.mleon.core.data.repository.ProductRepositoryImpl
import com.mleon.core.data.repository.interfaces.CartItemRepository
import com.mleon.core.data.repository.interfaces.OrdersRepository
import com.mleon.core.data.repository.interfaces.ProductRepository
import com.mleon.core.data.repository.interfaces.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Module
    @InstallIn(SingletonComponent::class)
    object RepositoryProvides { // Módulo anidado para los @Provides

        @Provides
        @Singleton
        fun provideUserRepository(apiService: UsersApiService): UserRemoteDataSource =
            UserRemoteDataSource(apiService)

        @Provides
        @Singleton
        fun provideOrdersRepository(apiService: OrderApiService): OrderRemoteDataSource =
            OrderRemoteDataSource(apiService)

        @Provides
        @Singleton
        fun provideProductRepositoryApi(apiService: ProductsApiService): ProductRemoteDataSource =
            ProductRemoteDataSource(apiService)
    }

    // --- BINDS DE INTERFACES A IMPLEMENTACIONES
    @Binds
    @Singleton
    abstract fun bindProductDataSource(impl: ProductRemoteDataSource): ProductDataSource

    @Binds
    @Singleton
    abstract fun bindProductRepository(impl: ProductRepositoryImpl): ProductRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRemoteDataSource): UserRepository

    @Binds
    @Singleton
    abstract fun bindOrdersRepository(impl: OrderRemoteDataSource): OrdersRepository

    @Binds
    @Singleton
    abstract fun bindCartItemRepository(impl: CartItemRepositoryImpl): CartItemRepository
}