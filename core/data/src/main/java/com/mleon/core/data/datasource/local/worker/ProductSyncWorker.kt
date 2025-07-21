package com.mleon.core.data.datasource.local.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mleon.core.data.di.ProductSyncWorkerEntryPoint
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.EntryPointAccessors

class ProductSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val entrypoint = EntryPointAccessors.fromApplication(
                applicationContext,
                ProductSyncWorkerEntryPoint::class.java
            )

            val productRepository = entrypoint.productRepository()
            productRepository.getProducts(refreshData = true)
            Result.success()
        } catch (e: Exception) {
            Log.e("ProductSyncWorker", "Error syncing products", e)
            Result.retry() // si falla sigue reintentando, no lo cancelamos.
        }

    }


}

