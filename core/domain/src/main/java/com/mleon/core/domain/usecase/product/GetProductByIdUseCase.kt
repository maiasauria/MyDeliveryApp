package com.mleon.core.domain.usecase.product

import com.mleon.core.data.repository.interfaces.ProductRepository
import com.mleon.core.model.Product
import javax.inject.Inject

class GetProductByIdUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(productId: String): Product? {
        // Llamamos al repositorio para obtener el producto por su ID
        return repository.getProductById(productId)
    }
}
