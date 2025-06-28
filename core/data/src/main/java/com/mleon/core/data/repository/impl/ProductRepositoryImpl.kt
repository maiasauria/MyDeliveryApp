package com.mleon.core.data.repository.impl

import android.util.Log
import com.mleon.core.data.datasource.ProductDataSource
import com.mleon.core.data.datasource.local.dao.ProductDao
import com.mleon.core.data.datasource.local.entities.ProductEntity
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
        Log.d("ProductRepositoryImpl", "Fetching products...")
        return withContext(Dispatchers.IO) {
            try {
                // Fetch products from source
                val products = productDataSource.getProducts()

                // Save products to local database
                val productEntities = products.map { product ->
                    ProductEntity(
                        id = product.id,
                        name = product.name,
                        description = product.description,
                        price = product.price,
                        imageUrl = product.imageUrl ?: "", // Handle null imageUrl
                        categories = product.category // Convert List<Categories> to List<String>

                    )
                }
                productDao.insertProducts(productEntities)

                // Return the products
                products

            } catch (e: Exception) {
                // If API fails, fallback to local database
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
                }
            }
        }
    }
}