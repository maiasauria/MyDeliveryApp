package com.mleon.mydeliveryapp.data.repository

import com.mleon.mydeliveryapp.data.model.Product

interface ProductRepository {
    fun getProducts(): List<Product>
    fun filterProducts( nombre: String): List<Product>
}