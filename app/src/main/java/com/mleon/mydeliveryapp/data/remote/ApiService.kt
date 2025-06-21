package com.mleon.mydeliveryapp.data.remote

import com.mleon.mydeliveryapp.data.model.RemoteProduct
import retrofit2.http.GET

interface ApiService {
    @GET("foods")
    suspend fun getProducts(): List<RemoteProduct>

}