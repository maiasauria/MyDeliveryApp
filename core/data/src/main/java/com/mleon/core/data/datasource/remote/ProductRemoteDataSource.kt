package com.mleon.core.data.datasource.remote

import com.mleon.core.data.datasource.ProductDataSource
import com.mleon.core.data.datasource.remote.model.toProduct
import com.mleon.core.data.remote.ProductsApiService
import com.mleon.core.model.Product
import javax.inject.Inject


// only fetches data and throws exceptions on failure
class ProductRemoteDataSource @Inject constructor( //Inyectamos el ApiService
    private val apiService: ProductsApiService
) : ProductDataSource {
    override suspend fun getProducts(): List<Product> {
        return apiService.getProducts().map { remoteProduct -> remoteProduct.toProduct() }
    }
}

