package com.mleon.utils.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.mleon.core.navigation.NavigationRoutes
import com.mleon.utils.R


@Composable
fun YappBottomBar(
    currentRoute: String?,
    onItemClick: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.bottom_bar_padding))
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomBarDestination.items.forEach { item ->
                val isSelected = currentRoute == item.route
                val highlightColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)

                Box(
                    modifier = Modifier
                        .width(dimensionResource(id = R.dimen.bottom_bar_item_width))
                        .height(dimensionResource(id = R.dimen.bottom_bar_item_height))
                        .background(
                            color = if (isSelected) highlightColor else MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(dimensionResource(id = R.dimen.bottom_bar_item_corner_radius))
                        )
                        .clickable { onItemClick(item.route)}
                        .padding(dimensionResource(id = R.dimen.bottom_bar_item_padding)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = if (isSelected) item.filledIcon else item.outlinedIcon,
                            contentDescription = item.label,
                            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                        )
                        if (isSelected) {
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}


sealed class BottomBarDestination(
    val route: String,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector,
    val label: String
) {
    object Cart : BottomBarDestination(
        NavigationRoutes.CART,
        Icons.Filled.ShoppingCart,
        Icons.Outlined.ShoppingCart,
        "Carrito"
    )
    object Home : BottomBarDestination(
        NavigationRoutes.PRODUCTS,
        Icons.Filled.Home,
        Icons.Outlined.Home,
        "Productos"
    )
    object Orders : BottomBarDestination(
        NavigationRoutes.ORDERS,
        Icons.Filled.Receipt,
        Icons.Outlined.Receipt,
        "Pedidos"
    )
    object Profile : BottomBarDestination(
        NavigationRoutes.PROFILE,
        Icons.Filled.Person,
        Icons.Outlined.Person,
        "Perfil"
    )
    companion object {
        val items = listOf(Home, Cart, Orders, Profile)
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