package com.mleon.appmodule.repositories

import com.mleon.appmodule.models.Product

interface ProductRepository {
    fun productList(): List<Product>
}