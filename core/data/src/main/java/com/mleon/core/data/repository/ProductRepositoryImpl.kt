package com.mleon.core.data.repository

import android.util.Log
import com.mleon.core.data.datasource.local.dao.ProductDao
import com.mleon.core.data.datasource.local.entities.ProductEntity
import com.mleon.core.data.datasource.remote.ProductRemoteDataSource
import com.mleon.core.data.repository.interfaces.ProductRepository
import com.mleon.core.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val productRemoteDataSource: ProductRemoteDataSource
) : ProductRepository {

    override suspend fun getProducts(): List<Product> {
        Log.d("ProductRepository", "Fetching products...")
        return withContext(Dispatchers.IO) {
            try {
                // Fetch products from remote API
                val remoteProducts = productRemoteDataSource.getProducts()

                // Save remote products to local database
                val productEntities = remoteProducts.map { product ->
                    ProductEntity(
                        id = product.id,
                        name = product.name,
                        description = product.description,
                        price = product.price,
                        imageUrl = product.imageUrl ?: "", // Handle null imageUrl
                    )
                }
                productDao.insertProducts(productEntities)

                // Return the remote products
                remoteProducts
            } catch (e: Exception) {
                // If API fails, fallback to local database
                productDao.getAllProducts().map { entity ->
                    Product(
                        id = entity.id,
                        name = entity.name,
                        description = entity.description,
                        price = entity.price,
                        imageUrl = entity.imageUrl,
                        includesDrink = false, //TODO: Adjust based on your model
                    )
                }
            }
        }
    }

    override suspend fun filterProducts(name: String): List<Product> {
        return withContext(Dispatchers.IO) {
            productDao.getAllProducts()
                .filter { it.name.contains(name, ignoreCase = true) }
                .map { entity ->
                    Product(
                        id = entity.id,
                        name = entity.name,
                        description = entity.description,
                        price = entity.price,
                        imageUrl = entity.imageUrl,
                        includesDrink = false, //TODO: Adjust based on your model
                    )
                }
        }
    }

    override suspend fun filterProductsByCategory(category: String): List<Product> {
        // Implement filtering logic based on category if needed
        // Assuming you have a category relation in the database
        return emptyList()
    }
}