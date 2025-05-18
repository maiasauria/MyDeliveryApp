package com.mleon.appmodule.repositories

import com.mleon.appmodule.models.Product

class MockProductRepository : ProductRepository {
    override fun productList(): List<Product> {
        return listOf(
            Product(productId = "1", name = "Papas", category = "Verduleria", price = 10.99),
            Product(productId = "2", name = "Carne", category = "Carniceria", price = 15.49),
            Product(productId = "3", name = "Galletitas", category = "Almacen", price = 7.99)
        )
    }
}