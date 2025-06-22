package com.mleon.mydeliveryapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mleon.feature.cart.view.ui.views.CartScreen
import com.mleon.feature.cart.view.viewmodel.CartViewModel
import com.mleon.feature.profile.views.ProfileScreen
import com.mleon.mydeliveryapp.presentation.views.LoginScreen
import com.mleon.mydeliveryapp.presentation.views.ProductListScreen
import com.mleon.mydeliveryapp.presentation.views.SignupScreen
import com.mleon.core.navigation.NavigationRoutes

@Composable
fun AppNavigation(navController: NavHostController) {

    val cartViewModel: CartViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        //startDestination = "login" // Define la ruta inicial
        startDestination = NavigationRoutes.PRODUCTS //TODO Cambiar a "login" cuando se implemente la autenticación
    ) {

        // Aquí definimos las rutas de navegación
        composable(route = NavigationRoutes.LOGIN) { //a que archivo apuntamos
            LoginScreen(navController) // Pasamos el navController a LoginScreen
        }
        composable(route = NavigationRoutes.SIGNUP) {
            SignupScreen(navController)
        }
        composable(route = NavigationRoutes.PRODUCTS) {
            ProductListScreen( cartViewModel = cartViewModel)
        }
        composable(route = NavigationRoutes.CART) {
            CartScreen(navController, cartViewModel = cartViewModel)
        }
        composable(route = NavigationRoutes.PROFILE) {
            ProfileScreen()
        }
        composable(route = NavigationRoutes.ORDERS) {
        }
        composable(route = NavigationRoutes.PAYMENT) {
        }
    }
}