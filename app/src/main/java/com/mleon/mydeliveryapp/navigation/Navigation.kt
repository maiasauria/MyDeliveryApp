package com.mleon.mydeliveryapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mleon.feature.cart.view.ui.views.CartScreen
import com.mleon.feature.cart.view.viewmodel.CartViewModel
import com.mleon.mydeliveryapp.presentation.views.LoginScreen
import com.mleon.mydeliveryapp.presentation.views.ProductListScreen
import com.mleon.mydeliveryapp.presentation.views.ProfileScreen
import com.mleon.mydeliveryapp.presentation.views.SignupScreen

@Composable
fun AppNavigation(navController: NavHostController ) {

    NavHost(
        navController = navController,
        //startDestination = "login" // Define la ruta inicial
        startDestination = "products" //TODO Cambiar a "login" cuando se implemente la autenticación
    ) {

        // Aquí definimos las rutas de navegación
        composable(route = "login") { //a que archivo apuntamos
            LoginScreen(navController) // Pasamos el navController a LoginScreen
        }
        composable(route = "signup") {
            SignupScreen(navController)
        }
        composable(route = "products") { backStackEntry ->
            // Con remember obtenemos el viewmodel generado en products y lo pasamos a ProductListScreen
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("products")
            }
            val cartViewModel: CartViewModel = hiltViewModel(parentEntry)
            ProductListScreen(cartViewModel = cartViewModel)
        }
        composable(route = "cart") { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("products")
            }
            val cartViewModel: CartViewModel = hiltViewModel(parentEntry)
            CartScreen(navController, cartViewModel = cartViewModel)
        }

        composable(route = "profile") {
            ProfileScreen()
        }
        composable(route = "orders") {
        }
        composable(route = "payment") {
        }
    }
}