package com.mleon.core.data.datasource.local.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mleon.core.data.datasource.remote.model.ProductResult
import com.mleon.core.data.repository.interfaces.ProductRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

class ProductSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
) : CoroutineWorker(context, params) {

    //TODO revisar en que paquete se debe crear el worker, si en feature o en core
    //TODO usar el worker para sincronizar la imagen de cloudinary

    override suspend fun doWork(): Result {
        return try {
            val entrypoint = EntryPointAccessors.fromApplication(
                applicationContext,
                ProductSyncWorkerEntryPoint::class.java
            )
            val getProductsUseCase = entrypoint.getProductsUseCase()
            getProductsUseCase.invoke(refreshData = true)
            Result.success()
        } catch (e: Exception) {
            Log.e("ProductSyncWorker", "Error syncing products", e)
            Result.retry() // si falla sigue reintentando, no lo cancelamos.
        }

    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface ProductSyncWorkerEntryPoint {
        // no se puede inyectar directamente el use case, se debe usar un entrypoint
        fun getProductsUseCase(): GetProductsUseCase
        // fun getProfileUseCase(): GetProfileUseCase
    }

//TODO revisar el refreshdata
    class GetProductsUseCase @Inject constructor(
        private val productRepository: ProductRepository
    ) {
        suspend operator fun invoke(refreshData: Boolean): ProductResult {
            return productRepository.getProducts(refreshData)
        }
    }
    }