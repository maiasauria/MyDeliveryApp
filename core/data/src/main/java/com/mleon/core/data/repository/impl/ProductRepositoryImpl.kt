package com.mleon.core.data.repository.impl

import com.mleon.core.data.datasource.ProductDataSource
import com.mleon.core.data.datasource.local.dao.ProductDao
import com.mleon.core.data.datasource.local.entities.toModel
import com.mleon.core.data.datasource.local.entities.toProductEntity
import com.mleon.core.model.result.ProductResult
import com.mleon.core.data.repository.interfaces.ProductRepository
import com.mleon.core.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

// Esta clase implementa un DataSource que puede ser Remoto o Local
// Y guarda los productos en Room
class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val productDataSource: ProductDataSource
) : ProductRepository {

    override suspend fun getProducts(refreshData: Boolean): ProductResult {
        return if (refreshData) {
            fetchAndStoreProducts() // Si se solicita una actualización, obtengo los productos y los guardo
        } else {
            getProductsFromDatabase() // Si no, devuelvo los productos de la base de datos
        }
    }

    private suspend fun fetchAndStoreProducts(): ProductResult {
        return withContext(Dispatchers.IO) {
                // Traigo los productos desde la fuente
                when (val result = productDataSource.getProducts()) {
                    is ProductResult.Success -> {
                        val products = result.products
                        val existingIds = productDao.getAllProducts().map { it.id }.toSet() // Me fijo en Room si ya existen productos con los mismos IDs

                        // Mapeo solo los nuevos productos que no están en la base de datos
                        val newProductEntities = products
                            .filter { product -> product.id !in existingIds }
                            .map { it.toProductEntity() }

                        // Inserto solo los nuevos productos en la base de datos
                        if (newProductEntities.isNotEmpty()) {
                            productDao.insertProducts(newProductEntities)
                        }

                        // Devuelvo los productos que envio la fuente
                        ProductResult.Success(products)
                    }
                    is ProductResult.Error -> result // Propaga el error recibido
                }

        }
    }

    private suspend fun getProductsFromDatabase(): ProductResult {
        return withContext(Dispatchers.IO) {
            val localProducts =
                productDao.getAllProducts() // Obtengo los productos de la base de datos
            if (localProducts.isNotEmpty()) {
                ProductResult.Success(localProducts.map { it.toModel() }) // Si hay productos, los convierto a modelo
            } else {
                fetchAndStoreProducts()
            }
        }
    }

    override suspend fun getProductById(productId: String): Product? {
        return withContext(Dispatchers.IO) {
            productDao.getProductById(productId)?.toModel()
        }
    }
}