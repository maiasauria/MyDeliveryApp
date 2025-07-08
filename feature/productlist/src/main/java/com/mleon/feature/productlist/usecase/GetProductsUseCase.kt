package com.mleon.feature.productlist.usecase

import com.mleon.core.data.repository.interfaces.ProductRepository
import com.mleon.core.model.Product
import javax.inject.Inject


class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(): List<Product> {
        return productRepository.getProducts()
    }
}