package com.mleon.utils

import java.text.DecimalFormat

fun Double.toCurrencyFormat(): String {
    val formatter = DecimalFormat("#,###")
    return formatter.format(this)
}