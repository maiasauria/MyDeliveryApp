package com.mleon.core.data.datasource.remote

import com.mleon.core.data.datasource.ProductDataSource
import com.mleon.core.data.datasource.remote.model.RemoteProduct
import com.mleon.core.data.remote.ProductsApiService
import com.mleon.core.model.Product
import com.mleon.core.model.enums.Categories
import javax.inject.Inject


// only fetches data and throws exceptions on failure
class ProductRemoteDataSource @Inject constructor( //Inyectamos el ApiService
    private val apiService: ProductsApiService
) : ProductDataSource {
    override suspend fun getProducts(): List<Product> {
        return apiService.getProducts().map { remoteProduct -> remoteProduct.toProduct() }
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