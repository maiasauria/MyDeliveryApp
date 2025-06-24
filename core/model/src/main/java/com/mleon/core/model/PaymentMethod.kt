package com.mleon.core.model

enum class PaymentMethod(val displayName: String, val apiValue: String) {
    CASH("Efectivo", "CASH"),
    CREDIT_CARD("Tarjeta de Credito", "CREDIT_CARD");

    companion object {
        fun fromDisplayName(name: String?): PaymentMethod? =
            values().find { it.displayName.equals(name, ignoreCase = true) }
        fun fromApiValue(value: String?): PaymentMethod? =
            values().find { it.apiValue.equals(value, ignoreCase = true) }
    }
}