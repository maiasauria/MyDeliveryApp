package com.mleon.core.data.datasource.remote.service

import com.mleon.core.data.datasource.remote.model.ProductResponse
import retrofit2.http.GET

interface ProductsApiService {
    @GET("foods")
    suspend fun getProducts(): ProductResponse

    //TODO getproducts by Id
}