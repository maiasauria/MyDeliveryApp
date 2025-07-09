package com.mleon.core.data.datasource.remote

import com.mleon.core.data.datasource.ProductDataSource
import com.mleon.core.data.datasource.remote.model.toDomain
import com.mleon.core.data.model.ProductResult
import com.mleon.core.data.remote.ProductsApiService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

// Obtiene los datos de los productos desde una fuente remota (API)
// Mapea los datos del ApiService a los del dominio, porque tenemos otras implementaciones de ProductDataSource que no son remotas
class ProductRemoteDataSource @Inject constructor( //Inyectamos el ApiService
    private val apiService: ProductsApiService
) : ProductDataSource {
    override suspend fun getProducts(): ProductResult {
        return try {
            val products =
                apiService.getProducts().map { remoteProduct -> remoteProduct.toDomain() }
            ProductResult.Success(products)

        } catch (e: IOException) {
            ProductResult.Error("Sin conexión a Internet. Verifica tu conexión e intenta nuevamente")
        } catch (e: HttpException) {
            ProductResult.Error("Error del servidor: ${e.code()}")
        } catch (e: Exception) {
            ProductResult.Error("Ocurrió un error inesperado")
        }
    }
}

