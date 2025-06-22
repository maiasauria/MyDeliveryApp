package com.mleon.core.data.model

import com.google.gson.annotations.SerializedName

data class ProductsResponse (
   @SerializedName("status") val status: String,
   @SerializedName("message") val products: List<String>,

)