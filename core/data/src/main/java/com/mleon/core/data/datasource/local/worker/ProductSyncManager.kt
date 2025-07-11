package com.mleon.core.data.datasource.local.worker

import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ProductSyncManager @Inject constructor(
    private val workManagerHelper: WorkManagerHelper,
) {

    fun schedulePeriodicSync() {
        workManagerHelper.schedulePeriodicTask<ProductSyncWorker>(
            uniqueWorkName = "PRODUCT_SYNC_WORK",
            repeatInterval = 15,
            timeUnit = TimeUnit.MINUTES,
        )
    }

    fun syncNow() {
        workManagerHelper.scheduleOneTimeTask<ProductSyncWorker>(
            uniqueWorkName = "PRODUCT_SYNC_NOW",
        )
    }

}