package com.mleon.mydeliveryapp.view.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mleon.mydeliveryapp.ui.views.LoginScreen
import com.mleon.mydeliveryapp.ui.views.ProductListScreen
import com.mleon.mydeliveryapp.ui.views.SignupScreen

@Composable
fun AppNavigation( ) {
    val navController : NavHostController = rememberNavController() //remember para almacenar el estado de la variable
    NavHost(
        navController = navController,
        //startDestination = "login" // Define la ruta inicial
        startDestination = "products" //TODO Cambiar a "login" cuando se implemente la autenticación
    ) {
        // Aquí definimos las rutas de navegación
        composable(route = "login") { //a que archivo apuntamos
            LoginScreen(navController) // Pasamos el navController a LoginScreen
        }
        composable(route = "signup") { //a que archivo apuntamos
            SignupScreen(navController) // Pasamos el navController a LoginScreen
        }
        composable(route = "products") { //a que archivo apuntamos
            ProductListScreen(navController) // Pasamos el navController a ProductsScreen
        }
        composable(route = "cart") {
//            val productsViewModel : ProductListViewModel = hiltViewModel() // Obtenemos el ViewModel
//            val state = productsViewModel.productState.collectAsState() // Obtenemos el estado del ViewModel
//            ProductListView(
//                state = state.value,
//                innerPadding = PaddingValues(0.dp)) // Pasamos el navController a ProductsScreen
        }
    }
}