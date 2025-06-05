package com.mleon.mydeliveryapp.view.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mleon.mydeliveryapp.view.viewmodel.ProductListViewModel
import com.mleon.mydeliveryapp.view.ui.views.LoginScreen
import com.mleon.mydeliveryapp.view.ui.views.ProductListView

@Composable
fun AppNavigation( ) {
    val navController : NavHostController = rememberNavController() //remember para almacenar el estado de la variable
    NavHost(
        navController = navController,
        startDestination = "login" // Define la ruta inicial
    ) {
        // Aquí definimos las rutas de navegación
        composable(route = "login") { //a que archivo apuntamos
            LoginScreen(navController) // Pasamos el navController a LoginScreen
        }
        composable(route = "signup") { //a que archivo apuntamos
            LoginScreen(navController) // Pasamos el navController a LoginScreen
        }
        composable(route = "products") { //a que archivo apuntamos
            val productsViewModel : ProductListViewModel = hiltViewModel() // Obtenemos el ViewModel
            val state = productsViewModel.productState.collectAsState() // Obtenemos el estado del ViewModel
            ProductListView(
                state = state.value,
                innerPadding = PaddingValues(0.dp)) // Pasamos el navController a ProductsScreen
        }
    }
}