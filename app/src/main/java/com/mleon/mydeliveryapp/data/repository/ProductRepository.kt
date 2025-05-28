package com.mleon.mydeliveryapp.data.repository

import com.mleon.core.model.Product

interface ProductRepository {
    fun getProducts(): List<com.mleon.core.model.Product>
    fun filterProducts( nombre: String): List<com.mleon.core.model.Product>
}