package com.mleon.mydeliveryapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mleon.core.navigation.NavigationRoutes
import com.mleon.feature.cart.view.ui.views.CartScreen
import com.mleon.feature.checkout.view.CheckoutScreen
import com.mleon.feature.orders.view.OrdersScreen
import com.mleon.feature.productlist.view.ProductDetailScreen
import com.mleon.feature.productlist.view.ProductListScreen
import com.mleon.feature.profile.views.ProfileScreen
import com.mleon.feature.signup.view.SignupScreen
import com.mleon.login.view.LoginScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.LOGIN,
    ) {
        // Aquí definimos las rutas de navegación
        composable(route = NavigationRoutes.LOGIN) {
            LoginScreen(navController) // Pasamos el navController a LoginScreen
        }
        composable(route = NavigationRoutes.SIGNUP) {
            SignupScreen(navController)
        }
        composable(route = NavigationRoutes.PRODUCTS) {
            ProductListScreen(
                onProductClick = { productId ->
                    val route = NavigationRoutes.PRODUCT_DETAIL.replace("{productId}", productId)
                    navController.navigate(route)
                })
        }
        composable(route = NavigationRoutes.CART) {
            CartScreen(navController)
        }
        composable(route = NavigationRoutes.PROFILE) {
            ProfileScreen(navController)
        }
        composable(route = NavigationRoutes.ORDERS) {
            OrdersScreen()
        }
        composable(route = NavigationRoutes.CHECKOUT) {
            CheckoutScreen(navController)
        }
        composable(route = NavigationRoutes.PRODUCT_DETAIL) { backStackEntry ->
            ProductDetailNav(navController, backStackEntry)
        }
    }
}
@Composable
fun ProductDetailNav(
    navController: NavHostController,
    backStackEntry: NavBackStackEntry
) {
    val productId = backStackEntry.arguments?.getString("productId") ?: ""
    ProductDetailScreen(
        productId = productId,
        navController = navController,
    )
}

// Funcion de extensión para navegar a una ruta específica
    fun NavController.navigateToRoute(route: String) {
    this.navigate(route) {
        // Limpia la pila de navegación hasta el destino inicial
        popUpTo(this@navigateToRoute.graph.findStartDestination().id) {
            saveState = true // Guarda el estado de la pantalla actual
        }
        launchSingleTop = true // Evita crear múltiples instancias de la misma pantalla
        restoreState = true // Restaura el estado guardado de la pantalla si está disponible
    }
}
