package com.mleon.core.data.remote

import com.mleon.core.data.datasource.remote.model.RemoteProduct
import retrofit2.http.GET

interface ProductsApiService {
    @GET("foods")
    suspend fun getProducts(): List<RemoteProduct>

    //TODO getproducts by Id
}