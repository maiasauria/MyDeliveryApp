package com.mleon.core.data.datasource.remote

import com.mleon.core.data.datasource.ProductDataSource
import com.mleon.core.data.datasource.remote.dto.toDomain
import com.mleon.core.data.datasource.remote.service.ProductsApiService
import com.mleon.core.model.result.ProductResult
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val ERROR_NO_PRODUCTS = "No se encontraron productos"
private const val ERROR_NO_CONNECTION = "Sin conexión a Internet. Verifica tu conexión e intenta nuevamente"
private const val ERROR_SERVER = "Error del servidor: "
private const val ERROR_UNEXPECTED = "Ocurrió un error inesperado"

// Obtiene los datos de los productos desde una fuente remota (API)
// Mapea los datos del ApiService a los del dominio, porque tenemos otras implementaciones de ProductDataSource que no son remotas
class ProductRemoteDataSource @Inject constructor( //Inyectamos el ApiService
    private val apiService: ProductsApiService
) : ProductDataSource {
    override suspend fun getProducts(): ProductResult {
        return try {
            val response = apiService.getProducts()
            return if (response.isNotEmpty()) {
                ProductResult.Success(response.map { it.toDomain() })
            } else {
                ProductResult.Error(ERROR_NO_PRODUCTS)
            }
        } catch (e: IOException) {
            ProductResult.Error(ERROR_NO_CONNECTION)
        } catch (e: HttpException) {
            ProductResult.Error("$ERROR_SERVER ${e.code()}")
        } catch (e: Exception) {
            ProductResult.Error(ERROR_UNEXPECTED)
        }
    }
}

