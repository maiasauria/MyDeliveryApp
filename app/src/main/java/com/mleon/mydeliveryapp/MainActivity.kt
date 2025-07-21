package com.mleon.mydeliveryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mleon.mydeliveryapp.presentation.views.MainScreen
import com.mleon.mydeliveryapp.ui.theme.YappTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            YappTheme {
                val navController = rememberNavController() // mantiene el estado en recomposiciones
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute =
                    navBackStackEntry?.destination?.route // obtener la ruta actual desde el stack.
                MainScreen(navController, currentRoute)
            }
        }
    }
}

