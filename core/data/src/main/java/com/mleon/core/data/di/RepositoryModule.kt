package com.mleon.core.data.di

import com.mleon.core.data.datasource.OrderDataSource
import com.mleon.core.data.datasource.ProductDataSource
import com.mleon.core.data.datasource.UserDataSource
import com.mleon.core.data.datasource.remote.OrderRemoteDataSource
import com.mleon.core.data.datasource.remote.ProductRemoteDataSource
import com.mleon.core.data.datasource.remote.UserRemoteDataSource
import com.mleon.core.data.datasource.remote.service.OrderApiService
import com.mleon.core.data.datasource.remote.service.ProductsApiService
import com.mleon.core.data.datasource.remote.service.UsersApiService
import com.mleon.core.data.image.CloudinaryImageUploader
import com.mleon.core.data.repository.impl.CartItemRepositoryImpl
import com.mleon.core.data.repository.impl.OrderRepositoryImpl
import com.mleon.core.data.repository.impl.ProductRepositoryImpl
import com.mleon.core.data.repository.impl.UserRepositoryImpl
import com.mleon.core.data.repository.interfaces.CartItemRepository
import com.mleon.core.data.repository.interfaces.ImageUploader
import com.mleon.core.data.repository.interfaces.OrderRepository
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
    abstract fun bindUserDataSource(impl: UserRemoteDataSource): UserDataSource

    @Binds
    @Singleton
    abstract fun bindOrderDataSource(impl: OrderRemoteDataSource): OrderDataSource

    @Binds
    @Singleton
    abstract fun bindProductRepository(impl: ProductRepositoryImpl): ProductRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindOrdersRepository(impl: OrderRepositoryImpl): OrderRepository

    @Binds
    @Singleton
    abstract fun bindCartItemRepository(impl: CartItemRepositoryImpl): CartItemRepository

    @Binds
    @Singleton
    abstract fun bindImageUploader(impl: CloudinaryImageUploader): ImageUploader
}