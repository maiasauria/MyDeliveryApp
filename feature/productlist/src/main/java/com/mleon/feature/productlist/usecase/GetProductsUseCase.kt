package com.mleon.feature.productlist.usecase

import com.mleon.core.data.model.ProductResult
import com.mleon.core.data.repository.interfaces.ProductRepository
import javax.inject.Inject


class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(refreshData : Boolean): ProductResult {
        return productRepository.getProducts(refreshData)
    }
}