package com.mleon.core.data.datasource.remote.model

import com.mleon.core.data.datasource.remote.dto.ProductDto

data class ProductResponse(
    val message: String,
    val products: List<ProductDto>?
)