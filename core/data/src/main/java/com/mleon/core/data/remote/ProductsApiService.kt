package com.mleon.core.data.remote

import com.mleon.core.data.datasource.remote.model.ProductDto
import retrofit2.http.GET

interface ProductsApiService {
    @GET("foods")
    suspend fun getProducts(): List<ProductDto>

    //TODO getproducts by Id
}