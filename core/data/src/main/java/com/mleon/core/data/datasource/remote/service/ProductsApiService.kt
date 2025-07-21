package com.mleon.core.data.datasource.remote.service

import com.mleon.core.data.datasource.remote.dto.ProductDto
import retrofit2.http.GET

interface ProductsApiService {
    @GET("foods")
    suspend fun getProducts(): List<ProductDto>
}