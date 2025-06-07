package com.mleon.utils.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mleon.core.model.Categories
import com.mleon.core.model.Product
import com.mleon.utils.R
import com.mleon.utils.toCurrencyFormat

@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    product: Product,
    showQuantitySelector: Boolean = true,
    //  onActionClick: (Product, Int) -> Unit,
    actionButtonText: String
) {
    var quantity by remember { mutableIntStateOf(1) }

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(CornerSize(10.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(product.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = product.name,
                modifier = Modifier
                    .height(140.dp)
                    .width(120.dp)
                    .clip(RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp)),
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.ic_launcher_background),
                placeholder = painterResource(R.drawable.ic_launcher_background)
            )

            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {

                Text(
                    text = product.name,
                    modifier = Modifier.padding(bottom = 4.dp),
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = product.description,
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .fillMaxHeight(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween){

                    Text(
                        text = "\$${product.price.toCurrencyFormat()}",
                        modifier = Modifier
                            .padding(bottom = 8.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    IconButton(onClick = { /* Handle click */ }) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add",
                                tint = Color.White,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductCardPreview() {
    ProductCard(
        product = Product(
            id = 1,
            name = "Producto de Ejemplo",
            description = "Descripción del producto de ejemplo.",
            price = 19.99,
            imageUrl = "",
            includesDrink = false,
            category = listOf(Categories.VEGAN)
        ),
        showQuantitySelector = false,
        actionButtonText = "Agregar al carrito"
    )
}