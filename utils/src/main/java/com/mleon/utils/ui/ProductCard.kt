package com.mleon.utils.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mleon.core.model.Product
import com.mleon.utils.R

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
            .padding(horizontal = 8.dp, vertical = 5.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(CornerSize(10.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(product.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = product.name,
                modifier = Modifier
                    .height(160.dp)
                    .width(120.dp)
                    .padding(bottom = 8.dp),
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.ic_launcher_background)
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {

                Text(
                    text = product.name,
                    modifier = Modifier.padding(bottom = 4.dp),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = product.description,
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "\$${product.price}",
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .align(Alignment.End),
                    style = MaterialTheme.typography.bodyMedium
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (showQuantitySelector) {
                        Button(onClick = { if (quantity > 1) quantity-- }) { Text("-") }
                        Text(
                            text = quantity.toString(),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        Button(onClick = { quantity++ }) { Text("+") }
                    }
                    Button(
                        //            onClick = { onActionClick(product, quantity) },
                        onClick = { /* Handle action click */ },
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .weight(1f)
                    ) {
                        Text(actionButtonText)
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
            name = "Sample Product",
            description = "This is a sample product description.",
            price = 19.99,
            includesDrink = true,
            imageUrl = "https://i.imgur.com/EJLFNOwg.jpg"
        ),
        actionButtonText = "Add to Cart"
    )
}