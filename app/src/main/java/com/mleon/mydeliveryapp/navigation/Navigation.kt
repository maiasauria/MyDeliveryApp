package com.mleon.mydeliveryapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mleon.core.navigation.NavigationRoutes
import com.mleon.feature.cart.view.ui.views.CartScreen
import com.mleon.feature.checkout.view.CheckoutScreen
import com.mleon.feature.orders.view.OrdersListScreen
import com.mleon.feature.productlist.view.ProductListScreen
import com.mleon.feature.profile.views.ProfileScreen
import com.mleon.feature.signup.view.SignupScreen
import com.mleon.login.view.LoginScreen

/**
 * AppNavigation es la función que define las rutas de navegación de la aplicación.
 * Utiliza NavHost para gestionar las diferentes pantallas y sus transiciones.
 *
 * @param navController El controlador de navegación que se utiliza para navegar entre las diferentes pantallas de la aplicación.
 *  */

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.LOGIN,
    ) {
        // Aquí definimos las rutas de navegación
        composable(route = NavigationRoutes.LOGIN) {
            // a que ruta apuntamos
            LoginScreen(navController) // Pasamos el navController a LoginScreen
        }
        composable(route = NavigationRoutes.SIGNUP) {
            SignupScreen(navController)
        }
        composable(route = NavigationRoutes.PRODUCTS) {
            ProductListScreen()
        }
        composable(route = NavigationRoutes.CART) {
            CartScreen(navController)
        }
        composable(route = NavigationRoutes.PROFILE) {
            ProfileScreen()
        }
        composable(route = NavigationRoutes.ORDERS) {
            OrdersListScreen()
        }
        composable(route = NavigationRoutes.CHECKOUT) {
            CheckoutScreen(navController)
        }
    }
}

// Funcion de extensión para navegar a una ruta específica
    fun NavController.navigateToRoute(route: String) {
    this.navigate(route) {
        popUpTo(this@navigateToRoute.graph.findStartDestination().id) { // Limpia la pila de navegación hasta el destino inicial
            saveState = true // Guarda el estado de la pantalla actual
        }
        launchSingleTop = true // Evita crear múltiples instancias de la misma pantalla
        restoreState = true // Restaura el estado guardado de la pantalla si está disponible
    }
}
