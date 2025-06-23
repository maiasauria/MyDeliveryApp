package com.mleon.utils.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
    isLoading: Boolean,
    product: Product,
    onAddToCart: (Product) -> Unit = { },
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth(),
        shape = RoundedCornerShape(CornerSize(10.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(120.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model =
                    ImageRequest
                        .Builder(LocalContext.current)
                        .data(product.imageUrl)
                        .crossfade(true)
                        .build(),
                placeholder = painterResource(R.drawable.ic_launcher_background),
                contentDescription = product.name,
                modifier =
                    Modifier
                        .height(110.dp)
                        .width(110.dp)
                        .padding(end = 8.dp)
                        .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.ic_launcher_background),
            )

            Column(
                modifier =
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .fillMaxHeight(),
            ) {
                // TODO poner favoritos.
                Text(
                    text = product.name,
                    modifier = Modifier.padding(bottom = 4.dp),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = product.description,
                    modifier = Modifier.padding(bottom = 4.dp),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "\$${product.price.toCurrencyFormat()}",
                        modifier =
                            Modifier
                                .padding(bottom = 8.dp),
                        style = MaterialTheme.typography.bodyMedium,
                    )

                    IconButton(
                        onClick = { onAddToCart(product) },
                        enabled = !isLoading
                    ) {
                        Box(
                            modifier =
                                Modifier
                                    .size(40.dp)
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

@Composable
fun CartProductCard(
    modifier: Modifier = Modifier,
    product: Product,
    isLoading: Boolean,
    quantity: Int,
    onQuantityChange: (product: Product, quantity: Int) -> Unit = { _, _ -> },
    onRemoveFromCart: () -> Unit = {},
) {
    Card(
        modifier =
            modifier.fillMaxWidth(),
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
            AsyncImage(
                model =
                    ImageRequest
                        .Builder(LocalContext.current)
                        .data(product.imageUrl)
                        .crossfade(true)
                        .build(),
                contentDescription = product.name,
                modifier =
                    Modifier
                        .height(100.dp)
                        .width(100.dp)
                        .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.ic_launcher_background),
                placeholder = painterResource(R.drawable.ic_launcher_background),
            )
            Column(
                modifier =
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .fillMaxHeight(),
            ) {
                Text(
                    text = product.name,
                    modifier = Modifier.padding(bottom = 4.dp),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        modifier = Modifier.width(64.dp),
                        text = "\$${product.price.toCurrencyFormat()}",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(
                            onClick = {
                                if (quantity > 1) {
                                    onQuantityChange(product, quantity - 1)
                                }
                            },
                            enabled = quantity > 1 && !isLoading,
                        ) {
                            Box(
                                modifier =
                                    Modifier
                                        .size(35.dp)
                                        .background(
                                            MaterialTheme.colorScheme.primary,
                                            shape = CircleShape,
                                        ),
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Remove,
                                    contentDescription = "Remove",
                                    tint = if (isLoading) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.align(Alignment.Center),
                                )
                            }
                        }
                        Text(
                            text = quantity.toString(),
                            modifier = Modifier.padding(horizontal = 8.dp),
                        )
                        IconButton(
                            onClick = { onQuantityChange(product, quantity + 1) },
                            enabled = !isLoading,
                        ) {
                            Box(
                                modifier =
                                    Modifier
                                        .size(35.dp)
                                        .background(
                                            MaterialTheme.colorScheme.primary,
                                            shape = CircleShape,
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
                    RemoveFromCartButton(
                        modifier = Modifier.padding(start = 8.dp),
                        isLoading = isLoading,
                        onRemoveFromCart = onRemoveFromCart,
                    )
                }
            }
        }
    }
}

@Composable
private fun RemoveFromCartButton(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onRemoveFromCart: () -> Unit = {},
) {
    IconButton(
        onClick = onRemoveFromCart,
        modifier =
            Modifier
                .padding(start = 8.dp)
                .width(30.dp),
        enabled = !isLoading,
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            tint = if (isLoading) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
            contentDescription = "Remove from cart",
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun CartProductCardPreview() {
    CartProductCard(
        product =
            Product(
                id = "1",
                name = "Producto de prueba con un nombre largo",
                description = "Description del producto de prueba con un nombre largo",
                price = 19.99,
                imageUrl = "https://via.placeholder.com/150",
                includesDrink = true,
            ),
        isLoading = false,
        quantity = 2,
    )
}
