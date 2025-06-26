package com.mleon.mydeliveryapp.presentation.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.mleon.mydeliveryapp.navigation.AppNavigation
import com.mleon.mydeliveryapp.navigation.navigateToRoute
import com.mleon.utils.ui.YappBottomBar

@Composable
fun MainScreen(navController: NavHostController, currentRoute: String?) {
    Scaffold(
        bottomBar = {
            YappBottomBar(
                currentRoute = currentRoute,
                onItemClick = { route -> navController.navigateToRoute(route) }
            )
        }
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            AppNavigation(navController = navController)
        }
    }
}