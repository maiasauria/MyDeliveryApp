package com.mleon.core.domain.usecase.product

import com.mleon.core.data.repository.interfaces.ProductRepository
import com.mleon.core.model.result.ProductResult

import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(refreshData : Boolean): ProductResult {
        // Llamamos al repositorio para obtener la lista de productos
        return productRepository.getProducts(refreshData)
    }
}
