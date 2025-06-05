package com.mleon.mydeliveryapp.data.repository

import com.mleon.core.model.Product

class ProductRepositoryImpl : ProductRepository {

    private val products = listOf(
        Product(
            id = 1,
            name = "Hamburguesa con Papas",
            description = "Hamburguesa de res con papas fritas y bebida.",
            price = 15000.0,
            includesDrink = true,
            imageUrl = "",
            category = "Hamburguesas"
        ),
        Product(
            id = 2,
            name = "Pizza de Muzzarella",
            description = "Pizza de muzzarella con masa fina y crujiente.",
            price = 25000.0,
            includesDrink = false,
            imageUrl = "",
            category = "Pizza"
        ),
        Product(
            id = 3,
            name = "Fideos con Pesto y Pollo",
            description = "Fideos caseros de espinaca, con pesto de albahaca y pollo grillé",
            price = 23000.0,
            includesDrink = false,
            imageUrl = "",
            category = "Pastas"
        ),
        Product(
            id = 4,
            name = "Sorrentinos de Jamón y Queso",
            description = "Sorrentinos rellenos de jamón y queso con salsa de tomate.",
            price = 12000.0,
            includesDrink = true,
            imageUrl = "",
            category = "Pastas"
        ),
        Product(
            id = 5,
            name = "Ensalada César con Pollo",
            description = "Ensalada César con pollo, lechuga, crutones y aderezo.",
            price = 18000.0,
            includesDrink = false,
            imageUrl = "",
            category = "Ensaladas"
        ),
        Product(
            id = 6,
            name = "Falafel con Hummus",
            description = "Falafel con hummus, ensalada y pan pita.",
            price = 15000.0,
            includesDrink = false,
            imageUrl = "",
            category = "Vegana"
        ),
        Product(
            id = 7,
            name = "Tacos de Pollo",
            description = "Tacos de pollo con guacamole, cebolla y morrón.",
            price = 20000.0,
            includesDrink = true,
            imageUrl = "",
            category = "Mexicana"
        )
    )

    override fun getProducts(): List<Product> = products

    override fun filterProducts(nombre: String): List<Product> {
        return products.filter {
            it.name.contains(nombre, ignoreCase = true) ||
                    it.description.contains(nombre, ignoreCase = true)
        }
    }
}

