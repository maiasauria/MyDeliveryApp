package com.mleon.mydeliveryapp.data.repository

import com.mleon.core.model.Categories
import com.mleon.core.model.Product

class ProductRepositoryImpl : ProductRepository {

    private val products = listOf(
        Product(
            id = 1,
            name = "Hamburguesa con Papas",
            description = "Hamburguesa de res con papas fritas y bebida.",
            price = 15000.0,
            includesDrink = true,
            imageUrl = "https://img.freepik.com/free-photo/front-view-burger-stand_141793-15542.jpg",
            category = listOf(Categories.BURGER, )
        ),
        Product(
            id = 2,
            name = "Pizza de Muzzarella",
            description = "Pizza de muzzarella.",
            price = 25000.0,
            includesDrink = false,
            imageUrl = "https://img.freepik.com/free-photo/pepperoni-pizza-table_140725-5373.jpg",
            category = listOf(Categories.PIZZA, Categories.VEGETARIAN)
        ),
        Product(
            id = 3,
            name = "Fideos con Pesto y Pollo",
            description = "Fideos caseros de espinaca, con pesto de albahaca y pollo grillé",
            price = 23000.0,
            includesDrink = false,
            imageUrl = "https://img.freepik.com/free-photo/delicious-pesto-pasta-plate_23-2150690777.jpg",
            category = listOf(Categories.PASTA)
        ),
        Product(
            id = 4,
            name = "Sorrentinos de Jamón y 4 Quesos",
            description = "Sorrentinos rellenos de jamón y queso con salsa de tomate.",
            price = 12000.0,
            includesDrink = true,
            imageUrl = "https://img.freepik.com/premium-photo/close-up-food-plate-table_1048944-24298741.jpg",
            category = listOf(Categories.PASTA)
        ),
        Product(
            id = 5,
            name = "Ensalada César con Pollo",
            description = "Ensalada César con pollo, lechuga, crutones y aderezo.",
            price = 18000.0,
            includesDrink = false,
            imageUrl = "https://img.freepik.com/premium-photo/homemade-caesar-salad-with-chicken-croutons_186456-1032.jpg",
            category = listOf(Categories.SALAD)
        ),
        Product(
            id = 6,
            name = "Falafel con Hummus",
            description = "Falafel con hummus, ensalada y pan pita.",
            price = 15000.0,
            includesDrink = false,
            imageUrl = "https://img.freepik.com/free-photo/tortilla-wrap-with-falafel-fresh-salad-vegan-tacos-vegetarian-healthy-food_2829-14399.jpg",
            category = listOf(Categories.VEGAN, Categories.VEGETARIAN)
        ),
        Product(
            id = 7,
            name = "Tacos de Pollo",
            description = "Tacos de pollo con guacamole, cebolla y morrón.",
            price = 20000.0,
            includesDrink = true,
            imageUrl = "https://images.unsplash.com/photo-1551504734-5ee1c4a1479b?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            category = listOf(Categories.MEXICAN, )
        ),
        Product(
         id = 8,
            category = listOf(Categories.VEGAN, Categories.SALAD),
            name = "Ensalada Vegana con Quinoa",
            description = "Ensalada vegana con quinoa, espinacas, tomate cherry y aderezo de mostaza.",
            price = 16000.0,
            includesDrink = false,
            imageUrl = "https://img.freepik.com/free-photo/side-view-quinoa-salad-with-tomato-cucumber-basil-salt-pepper-table_141793-3676.jpg?t=st=1749301264~exp=1749304864~hmac=a7ab0cb1489afa583da5d148518fd12217076ec10e72a26647d90f252d4c2b42&w=1380"
        ),
        Product(
            id = 9,
            name = "Hamburguesa de Lentejas",
            description = "Hamburguesa vegana de lentejas con pan integral y ensalada.",
            price = 14000.0,
            includesDrink = false,
            imageUrl = "https://img.freepik.com/free-photo/sandwich-hamburger-with-juicy-burgers-tomato-red-cabbage_2829-4118.jpg?t=st=1749301224~exp=1749304824~hmac=5b81e216ce1a8ff0694f10d06ae29030b82a651c9b457a774c4d1e7caa9ddcbf&w=1380",
            category = listOf(Categories.BURGER, Categories.VEGAN, Categories.VEGETARIAN)
        ),
        Product(
            id = 10,
            name = "Burrito de Carne Asada",
            description = "Burrito de carne asada con arroz, guacamole y salsa.",
            price = 27000.0,
            includesDrink = false,
            imageUrl = "https://img.freepik.com/free-photo/close-up-burrito-with-vegetables_23-2151915232.jpg?t=st=1749301160~exp=1749304760~hmac=500f3e573d42e43c546f15bdc97f9baa47c5bef3847599eb4fe1dec791dede72&w=1380",
            category = listOf(Categories.MEXICAN)
        )
    )

    override fun getProducts(): List<Product> = products

    override fun filterProducts(nombre: String): List<Product> {
        return products.filter {
            it.name.contains(nombre, ignoreCase = true) ||
                    it.description.contains(nombre, ignoreCase = true)
        }
    }

    override fun filterProductsByCategory(category: String): List<Product> {
        TODO("Not yet implemented")
    }


}

