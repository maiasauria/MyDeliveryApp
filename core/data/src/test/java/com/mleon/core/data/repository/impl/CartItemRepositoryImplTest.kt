
import app.cash.turbine.test
import com.mleon.core.data.datasource.local.dao.CartItemDao
import com.mleon.core.data.datasource.local.entities.CartItemEntity
import com.mleon.core.data.datasource.local.entities.CartItemWithProductEntity
import com.mleon.core.data.datasource.local.entities.ProductEntity
import com.mleon.core.data.repository.impl.CartItemRepositoryImpl
import com.mleon.core.model.enums.Categories
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class CartItemRepositoryImplTest {

    private lateinit var cartItemDao: CartItemDao
    private lateinit var repository: CartItemRepositoryImpl

    @Before
    fun setup() {
        cartItemDao = mock(CartItemDao::class.java)
        repository = CartItemRepositoryImpl(cartItemDao)
    }

    @Test
    fun `getAllCartItems with empty cart`() = runTest {
        `when`(cartItemDao.getAllCartItems()).thenReturn(flowOf(emptyList()))
        repository.getAllCartItems().test {
            val items = awaitItem()
            assert(items.isEmpty())
            awaitComplete()
        }
    }

    @Test
    fun `getAllCartItems with non empty cart`() = runTest {
        val cartItems = listOf(CartItemEntity(1, "prod1", 2))
        `when`(cartItemDao.getAllCartItems()).thenReturn(flowOf(cartItems))
        repository.getAllCartItems().test {
            val items = awaitItem()
            assert(items == cartItems)
            awaitComplete()
        }
    }

    @Test
    fun `getCartItemByProductId with existing product`() = runTest {
        val cartItem = CartItemEntity(1, "prod1", 2)
        `when`(cartItemDao.getCartItemByProductId("prod1")).thenReturn(cartItem)
        val result = repository.getCartItemByProductId("prod1")
        assert(result == cartItem)
    }

    @Test
    fun `getCartItemByProductId with non existent product`() = runTest {
        `when`(cartItemDao.getCartItemByProductId("prodX")).thenReturn(null)
        val result = repository.getCartItemByProductId("prodX")
        assert(result == null)
    }

    @Test
    fun `insertCartItem new item`() = runTest {
        val cartItem = CartItemEntity(1, "prod1", 2)
        repository.insertCartItem(cartItem)
        verify(cartItemDao).insertCartItem(cartItem)
    }

    @Test
    fun `updateCartItem existing item`() = runTest {
        val cartItem = CartItemEntity(1, "prod1", 3)
        repository.updateCartItem(cartItem)
        verify(cartItemDao).updateCartItem(cartItem)
    }

    @Test
    fun `deleteCartItem existing item`() = runTest {
        repository.deleteCartItem(1)
        verify(cartItemDao).deleteCartItem(1)
    }

    @Test
    fun `deleteAllCartItems on empty cart`() = runTest {
        repository.deleteAllCartItems()
        verify(cartItemDao).deleteCartItems()
    }

    @Test
    fun `insertCartItems with empty list`() = runTest {
        repository.insertCartItems(emptyList())
        verify(cartItemDao).insertCartItems(emptyList())
    }

    @Test
    fun `insertCartItems with non empty list`() = runTest {
        val items = listOf(CartItemEntity(1, "prod1", 2), CartItemEntity(2, "prod2", 1))
        repository.insertCartItems(items)
        verify(cartItemDao).insertCartItems(items)
    }

    @Test
    fun `getAllCartItemsWithProducts with empty cart`() = runTest {
        `when`(cartItemDao.getCartItemsWithProducts()).thenReturn(flowOf(emptyList()))
        repository.getAllCartItemsWithProducts().test {
            val items = awaitItem()
            assert(items.isEmpty())
            awaitComplete()
        }
    }

    @Test
    fun `getAllCartItemsWithProducts with non empty cart`() = runTest {
        val items = listOf(CartItemWithProductEntity(
            cartItemEntity = CartItemEntity(1, "prod1", 3),
            productEntity = ProductEntity(
                "prod1", "Product 1", "Description", 100.0, "Description 1",
                categories =  listOf(Categories.VEGETARIAN)
            ),
        ))
        `when`(cartItemDao.getCartItemsWithProducts()).thenReturn(flowOf(items))
        repository.getAllCartItemsWithProducts().test {
            val result = awaitItem()
            assert(result == items)
            awaitComplete()
        }
    }
}