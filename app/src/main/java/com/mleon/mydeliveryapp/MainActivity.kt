package com.mleon.mydeliveryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mleon.core.data.datasource.local.worker.ProductSyncManager
import com.mleon.mydeliveryapp.presentation.views.MainScreen
import com.mleon.mydeliveryapp.ui.theme.MyDeliveryAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var productSyncManager : ProductSyncManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val workRequest = OneTimeWorkRequestBuilder<DatabaseSyncWorker>().build()
//        WorkManager.getInstance(applicationContext).enqueue(workRequest)
        productSyncManager.schedulePeriodicSync()
        productSyncManager.syncNow()



        setContent {
            MyDeliveryAppTheme {
                val navController = rememberNavController() // preserve state across recompositions
                val navBackStackEntry by navController.currentBackStackEntryAsState() // get the current back stack entry
                val currentRoute = navBackStackEntry?.destination?.route // get the current route from the back stack entry
                MainScreen(navController, currentRoute)
            }
        }
    }
}
