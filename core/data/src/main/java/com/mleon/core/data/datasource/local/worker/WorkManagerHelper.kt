package com.mleon.core.data.datasource.local.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkManagerHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val workManager = WorkManager.getInstance(context)

    inline fun <reified T : CoroutineWorker> schedulePeriodicTask(
        uniqueWorkName: String,
        repeatInterval : Long = 15,
        timeUnit: TimeUnit = TimeUnit.MINUTES,
        networkRequired: Boolean = true
    ) {
        val constraints = Constraints.Builder()
            .apply {
                if (networkRequired) {
                    setRequiredNetworkType(NetworkType.CONNECTED)
                }
            }.build()

        val periodicWorkRequest = PeriodicWorkRequestBuilder<T>(
            repeatInterval,
            timeUnit
        ).setConstraints(constraints).build()

        workManager.enqueueUniquePeriodicWork(
            uniqueWorkName,
            ExistingPeriodicWorkPolicy.KEEP, // Mantener el trabajo existente si ya está programado
            periodicWorkRequest
        )
    }


    inline fun <reified T : CoroutineWorker> scheduleOneTimeTask(
        uniqueWorkName: String? = null, // Si no tiene nombre único, se encola como trabajo genérico
        networkRequired: Boolean = true
    ) {
        val constraints = Constraints.Builder()
            .apply {
                if (networkRequired) {
                    setRequiredNetworkType(NetworkType.CONNECTED)
                }
            }.build()

        val oneTimeWorkRequest = OneTimeWorkRequestBuilder<T>()
            .setConstraints(constraints)
            .build()

        if (uniqueWorkName != null) {
            workManager.enqueueUniqueWork(
                uniqueWorkName,
                ExistingWorkPolicy.REPLACE, // Reemplazamos el trabajo existente si ya está programado
                oneTimeWorkRequest
            )
        } else {
            workManager.enqueue(oneTimeWorkRequest) // Encolar el trabajo sin nombre único, generico
        }
    }

    fun cancelWork(uniqueWorkName: String) {
        workManager.cancelUniqueWork(uniqueWorkName)
    }
}

