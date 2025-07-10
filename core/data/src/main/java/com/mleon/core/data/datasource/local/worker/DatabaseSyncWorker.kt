package com.mleon.core.data.datasource.local.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mleon.core.data.datasource.local.AppDatabase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker // Para inyección de dependencias con Hilt
class DatabaseSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val appDatabase: AppDatabase
) : Worker(context, params) {

    override fun doWork(): Result {
        return try {
            val db = appDatabase.openHelper.writableDatabase
            //db.execSQL("ALTER TABLE users ADD COLUMN phone TEXT")
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}

