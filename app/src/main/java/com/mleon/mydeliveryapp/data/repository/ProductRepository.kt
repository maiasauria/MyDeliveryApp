package com.mleon.mydeliveryapp.data.repository

import com.mleon.core.model.Product

interface ProductRepository {
    fun getProducts(): List<Product>
    fun filterProducts( nombre: String): List<Product>
    fun filterProductsByCategory(category: String): List<Product>
}