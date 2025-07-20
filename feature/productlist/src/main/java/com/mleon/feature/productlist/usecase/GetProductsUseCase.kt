package com.mleon.feature.productlist.usecase

import com.mleon.core.data.datasource.remote.model.ProductResult
import com.mleon.core.data.repository.interfaces.ProductRepository
import javax.inject.Inject


class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(refreshData : Boolean): ProductResult {
        // Llamamos al repositorio para obtener la lista de productos
        return productRepository.getProducts(refreshData)
    }
}
