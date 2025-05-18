package com.mleon.mydeliveryapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MiApp: Application() {
    //funciona como un singleton, mientras la app está abierta esta clase está viva
    //se puede usar para inicializar librerías, como Firebase, etc.
    //no siempre la vamos a usar

    override fun onCreate() {
        super.onCreate()
        // Inicializar algo
    }
}