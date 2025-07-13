package com.mleon.feature.productlist.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mleon.core.model.Product
import com.mleon.core.model.enums.Categories
import com.mleon.utils.toCurrencyFormat
import com.mleon.utils.ui.ImageLoader

@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    product: Product,
    onAddToCart: (Product) -> Unit = { },
    onClick: (Product) -> Unit = { }
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable { onClick(product) },
        shape = RoundedCornerShape(CornerSize(10.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(110.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ImageLoader(
                url = product.imageUrl ?: "",
                contentDescription = product.name,
                modifier =
                    Modifier
                        .height(110.dp)
                        .width(110.dp)
            )
            Box(
                modifier = Modifier

                    .weight(1f)
                    .fillMaxHeight(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = product.description,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = product.price.toCurrencyFormat(),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                IconButton(
                    onClick = { onAddToCart(product) },
                    enabled = !isLoading,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            ),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductCardPreview() {
    ProductCard(
        product =
            Product(
                id = "1",
                name = "Producto de Ejemplo Con Nombre Largo",
                description = "Descripción del producto de ejemplo con Texto Largo.",
                price = 19.99,
                imageUrl = "https://img.freepik.com/free-photo/front-view-burger-stand_141793-15542.jpg",
                includesDrink = false,
                category = listOf(Categories.VEGAN),
            ),
        isLoading = false,
        onAddToCart = { },
    )
}

@Preview(showBackground = true)
@Composable
private fun ProductCardShortPreview() {
    ProductCard(
        product =
            Product(
                id = "1",
                name = "Producto de Ejemplo Con Nombre Largo",
                description = "Descripción del producto",
                price = 19.99,
                imageUrl = "https://img.freepik.com/free-photo/front-view-burger-stand_141793-15542.jpg",
                includesDrink = false,
                category = listOf(Categories.VEGAN),
            ),
        isLoading = false,
        onAddToCart = { },
    )
}