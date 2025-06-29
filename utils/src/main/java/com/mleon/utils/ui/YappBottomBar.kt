package com.mleon.utils.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.mleon.core.navigation.NavigationRoutes

@Composable
fun YappBottomBar(
    currentRoute: String?,
    onItemClick: (String) -> Unit
) {
    NavigationBar() {
        BottomBarDestination.items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onItemClick(item.route) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}
sealed class BottomBarDestination(val route: String, val icon: ImageVector, val label: String) {
    object Cart : BottomBarDestination(NavigationRoutes.CART, Icons.Filled.ShoppingCart, "Carrito")
    object Home : BottomBarDestination(NavigationRoutes.PRODUCTS, Icons.Filled.Home, "Productos")
    object Orders : BottomBarDestination(NavigationRoutes.ORDERS, Icons.Filled.Receipt, "Pedidos")
    object Profile : BottomBarDestination(NavigationRoutes.PROFILE, Icons.Filled.Person, "Perfil")
    companion object {
        val items = listOf( Home, Cart, Orders, Profile)
    }
}

@Preview
@Composable
fun YappBottomBarPreview() {
YappBottomBar(
    currentRoute = NavigationRoutes.PRODUCTS,
    onItemClick = {}
    )
}