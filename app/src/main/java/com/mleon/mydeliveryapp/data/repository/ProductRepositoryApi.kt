package com.mleon.mydeliveryapp.data.repository

import android.util.Log
import com.mleon.core.model.Categories
import com.mleon.core.model.Product
import com.mleon.mydeliveryapp.data.model.RemoteProduct
import com.mleon.mydeliveryapp.data.remote.ApiService

class ProductRepositoryApi(private val apiService: ApiService) : ProductRepository {
    override suspend fun getProducts(): List<Product> {
        Log.d("ProductRepositoryApi", "Fetching products from API")
        return apiService.getProducts().map { remoteProduct ->
            Log.d("ProductRepositoryApi", "Mapping remote product: $remoteProduct")
            remoteProduct.toProduct()
        }
        .also { products ->
            Log.d("ProductRepositoryApi", "Fetched products: $products")
        }
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
    Log.d("RemoteProduct", "Converting RemoteProduct to Product: $this")
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