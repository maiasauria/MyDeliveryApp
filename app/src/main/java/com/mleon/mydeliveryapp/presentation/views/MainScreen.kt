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

/*
* MainScreen es el punto de entrada de la aplicación.
* Tiene un Scaffold que incluye una barra inferior de navegación (YappBottomBar).
* Gestiona la navegación entre diferentes pantallas de la aplicación.
* @param navController El controlador de navegación que maneja la navegación entre las diferentes pantallas de la aplicación.
* @param currentRoute La ruta actual de la navegación, utilizada para resaltar el elemento activo en la barra inferior.
* */
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