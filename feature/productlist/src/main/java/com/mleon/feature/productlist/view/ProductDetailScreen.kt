
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mleon.core.model.Product
import com.mleon.feature.productlist.R
import com.mleon.feature.productlist.viewmodel.ProductDetailUiState
import com.mleon.feature.productlist.viewmodel.ProductDetailViewModel
import com.mleon.utils.ui.ImageLoader
import com.mleon.utils.ui.YappSmallLoadingIndicator

// ProductDetailScreen.kt
@Composable
fun ProductDetailScreen(
    viewModel: ProductDetailViewModel = hiltViewModel(),
    navController: NavController,
    productId: String,
    onAddToCart: (Product, Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var quantity by remember { mutableStateOf(1) }

    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    ProductDetailView(
        uiState = uiState,
        quantity = quantity,
        onQuantityChange = { quantity = it },
        onAddToCart = onAddToCart,
        onBack = {
            navController.popBackStack()
        }
    )
}



@Composable
fun ProductDetailView(
    uiState: ProductDetailUiState,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    onAddToCart: (Product, Int) -> Unit,
    onBack: () -> Unit // Add this parameter
) {
    when (uiState) {
        is ProductDetailUiState.Loading -> {
            YappSmallLoadingIndicator()
        }
        is ProductDetailUiState.Success -> {
            val product = uiState.product
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.33f) // Top 1/3rd
                ) {

                    ImageLoader(
                        url = product.imageUrl ?: "",
                        contentDescription = product.name,
                        modifier = Modifier.fillMaxSize(),
                    )

                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    ProductInfoView(product)
                    Spacer(modifier = Modifier.height(16.dp))
                    QuantitySelector(quantity, onQuantityChange)
                    Spacer(modifier = Modifier.height(16.dp))
                    AddToCartButton { onAddToCart(product, quantity) }
                }
            }
        }
        is ProductDetailUiState.Error -> {
            Text(text = stringResource(R.string.error_loading_product))
        }
    }
}

@Composable
fun ProductInfoView(product: Product) {
    Text(text = product.name, style = MaterialTheme.typography.headlineSmall)
    Text(text = product.description)
    Text(
        text = stringResource(
            id = R.string.product_detail_price,
            product.price
        )
    )
}

@Composable
fun QuantitySelector(quantity: Int, onQuantityChange: (Int) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = stringResource(R.string.product_detail_quantity))
        IconButton(onClick = { if (quantity > 1) onQuantityChange(quantity - 1) }) {
            Icon(Icons.Default.Remove, contentDescription = null)
        }
        Text(
            text = quantity.toString(),
            modifier = Modifier.width(32.dp),
            textAlign = TextAlign.Center
        )
        IconButton(onClick = { onQuantityChange(quantity + 1) }) {
            Icon(Icons.Default.Add, contentDescription = null)
        }
    }
}

@Composable
fun AddToCartButton(onAddToCart: () -> Unit) {
    Button(onClick = onAddToCart) {
        Text(text = stringResource(R.string.product_detail_add_to_cart))
    }
}