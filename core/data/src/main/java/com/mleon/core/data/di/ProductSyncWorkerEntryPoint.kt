package com.mleon.core.data.di

import com.mleon.core.data.repository.interfaces.ProductRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ProductSyncWorkerEntryPoint {
    // no se puede inyectar directamente el use case, se debe usar un entrypoint
    fun productRepository(): ProductRepository
}