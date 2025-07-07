package com.mleon.core.data.repository.impl

import com.mleon.core.data.datasource.ProductDataSource
import com.mleon.core.data.datasource.local.dao.ProductDao
import com.mleon.core.data.datasource.local.entities.toModel
import com.mleon.core.data.datasource.local.entities.toProductEntity
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

    override suspend fun getProducts(): List<Product> {

        return withContext(Dispatchers.IO) { // Cambia el contexto a IO para operaciones de base de datos y red
            try {
                // Traigo los productos desde la fuente
                val products = productDataSource.getProducts()

                // Me fijo en Room si ya existen productos con los mismos IDs
                val existingIds = productDao.getAllProducts().map { it.id }.toSet()

                // Mapeo solo los nuevos productos que no están en la base de datos
                val newProductEntities = products
                    .filter { product -> product.id !in existingIds }
                    .map { it.toProductEntity() }

                // Inserto solo los nuevos productos en la base de datos
                if (newProductEntities.isNotEmpty()) {
                    productDao.insertProducts(newProductEntities)
                }

                // Devuelvo los productos que tiene Room
                productDao.getAllProducts().map { it. toModel() }

            } catch (e: Exception) {
                // Si la API falla, traigo los productos de la base de datos
                productDao.getAllProducts().map { entity ->
                    Product(
                        id = entity.id,
                        name = entity.name,
                        description = entity.description,
                        price = entity.price,
                        imageUrl = entity.imageUrl,
                        includesDrink = false, //TODO: Implementar
                        category = entity.categories
                    )
                //emptyList<Product>()
                }
            }
        }
    }

    override suspend fun getProductById(productId: String): Product? {
        return withContext(Dispatchers.IO) {
            productDao.getProductById(productId)?.toModel()
        }
    }
    }