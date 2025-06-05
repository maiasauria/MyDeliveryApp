package com.mleon.mydeliveryapp.view.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mleon.core.model.Product

@Composable
fun ProductCard(
    product: Product,
    showQuantitySelector: Boolean = true,
    onActionClick: (Product, Int) -> Unit,
    actionButtonText: String,
    modifier: Modifier = Modifier
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
                    onClick = { onActionClick(product, quantity) },
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