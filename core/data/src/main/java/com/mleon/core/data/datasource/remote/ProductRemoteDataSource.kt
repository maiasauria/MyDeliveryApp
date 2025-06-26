package com.mleon.core.data.datasource.remote

import com.mleon.core.data.model.RemoteProduct
import com.mleon.core.data.remote.ProductsApiService
import com.mleon.core.data.repository.interfaces.ProductRepository
import com.mleon.core.model.Categories
import com.mleon.core.model.Product
import javax.inject.Inject

class ProductRemoteDataSource  @Inject constructor( //Inyectamos el ApiService
    private val apiService: ProductsApiService
) : ProductRepository {
    override suspend fun getProducts(): List<Product> {
        return apiService.getProducts().map { remoteProduct -> remoteProduct.toProduct() }
    }

    override suspend fun filterProducts(name: String): List<Product> {
        return getProducts().filter { product ->
            product.name.contains(name, ignoreCase = true)
        }
    }

    override suspend fun filterProductsByCategory(category: String): List<Product> {
        return getProducts().filter { product ->
            product.category.any { it.name == category }
        }
    }
}

fun RemoteProduct.toProduct(): Product {
    val safeCategory = categories?.mapNotNull { runCatching { Categories.valueOf(it) }.getOrNull() } ?: emptyList()
    return Product(
        id = _id,
        name = name,
        description = description ?: "",
        price = price,
        imageUrl = imageUrl ?: "",
        category = safeCategory,
        includesDrink = includesDrink ?: false,
    )
}