
import com.mleon.core.data.datasource.ProductDataSource
import com.mleon.core.data.datasource.local.dao.ProductDao
import com.mleon.core.data.datasource.local.entities.ProductEntity
import com.mleon.core.data.repository.impl.ProductRepositoryImpl
import com.mleon.core.model.Product
import com.mleon.core.model.result.ProductResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.anyList
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class ProductRepositoryImplTest {

 private lateinit var productDao: ProductDao
 private lateinit var productDataSource: ProductDataSource
 private lateinit var repository: ProductRepositoryImpl

 @Before
 fun setUp() {
  productDao = mock(ProductDao::class.java)
  productDataSource = mock(ProductDataSource::class.java)
  repository = ProductRepositoryImpl(productDao, productDataSource)
 }

 @Test
 fun `getProducts returns products from database when not refreshing`() = runTest {
  val entities = listOf(ProductEntity(
      "1", "Test", "Desc", 10.0,
      imageUrl = "",
      categories = listOf()
  ))
  `when`(productDao.getAllProducts()).thenReturn(entities)

  val result = repository.getProducts(refreshData = false)

  assertTrue(result is ProductResult.Success)
  assertEquals(1, (result as ProductResult.Success).products.size)
  verify(productDao).getAllProducts()
  verifyNoMoreInteractions(productDataSource)
 }

 @Test
 fun `getProducts fetches and stores products when refreshing`() = runTest {
  val products = listOf(Product(
      "2", "New", "Desc", 20.0, imageUrl = "",
      category = listOf(),
      includesDrink = false
  ))
  val productResult = ProductResult.Success(products)
  `when`(productDataSource.getProducts()).thenReturn(productResult)
  `when`(productDao.getAllProducts()).thenReturn(emptyList())

  val result = repository.getProducts(refreshData = true)

  assertTrue(result is ProductResult.Success)
  assertEquals("2", (result as ProductResult.Success).products.first().id)
  verify(productDataSource).getProducts()
  verify(productDao).insertProducts(anyList())
 }

 @Test
 fun `getProductById returns product if exists`() = runTest {
  val entity = ProductEntity("1", "Test", "Desc", 10.0,   imageUrl = "",
   categories = listOf())
  `when`(productDao.getProductById("1")).thenReturn(entity)

  val product = repository.getProductById("1")

  assertNotNull(product)
  assertEquals("1", product?.id)
  verify(productDao).getProductById("1")
 }

 @Test
 fun `getProducts propagates error from data source`() = runTest {
  val errorResult = ProductResult.Error("Network error")
  `when`(productDataSource.getProducts()).thenReturn(errorResult)
  `when`(productDao.getAllProducts()).thenReturn(emptyList())

  val result = repository.getProducts(refreshData = true)

  assertTrue(result is ProductResult.Error)
  assertEquals("Network error", (result as ProductResult.Error).message)
 }
}