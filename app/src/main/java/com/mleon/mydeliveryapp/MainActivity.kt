package com.mleon.mydeliveryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mleon.mydeliveryapp.navigation.AppNavigation
import com.mleon.mydeliveryapp.ui.theme.MyDeliveryAppTheme
import com.mleon.utils.ui.YappBottomBar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyDeliveryAppTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                Scaffold( //PAsar todo esto a otra clase.
                    bottomBar = {
                        YappBottomBar(
                            currentRoute = currentRoute,
                            onItemClick = { route ->
                                navController.navigate(route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true } // Save the state of the previous destinations
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                )  { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {
                        AppNavigation(navController = navController)
                    }
                }
            }
        }
    }
}
