package com.mleon.feature.productlist.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mleon.core.model.Product
import com.mleon.core.model.enums.Categories
import com.mleon.feature.cart.view.ui.views.QuantityButton
import com.mleon.feature.productlist.R
import com.mleon.utils.toCurrencyFormat
import com.mleon.utils.ui.ImageLoader
import com.mleon.utils.ui.ScreenSubTitle
import com.mleon.utils.ui.ScreenTitle

@Composable
fun ProductDetailView(
    product: Product,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    onAddToCart: (Product, Int) -> Unit = { _, _ -> },
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(dimensionResource(id = R.dimen.product_detail_fraction_height).value) // Top 1/3rd
        ) {
            ImageLoader(
                url = product.imageUrl ?: "",
                contentDescription = product.name,
                modifier = Modifier.fillMaxSize(),
            )
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.product_detail_back_button_radius)),
                    )
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.product_detail_back),
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.product_detail_section_spacing)))
            ProductInfoView(product)
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.product_detail_section_spacing)))

        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.product_detail_padding))
        ) {
            QuantitySelector(quantity, onQuantityChange)
            AddToCartButton { onAddToCart(product, quantity) }
        }
    }
}

@Composable
fun ProductInfoView(product: Product) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.product_detail_padding))
    ) {

        ScreenTitle(product.name)
        ScreenSubTitle(product.price.toCurrencyFormat())

        Text(text = product.description)
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.product_detail_section_spacing)))

        IncludesDrinkRow(product.includesDrink)

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.product_detail_section_spacing)))
        CategoryChips(product.category)
    }

}

@Composable
fun QuantitySelector(quantity: Int, onQuantityChange: (Int) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.product_detail_selector_radius))
            )
            .wrapContentHeight()
    ) {
        QuantityButton(
            onClick = {
                if (quantity > 1) {
                    onQuantityChange(quantity - 1)
                }
            },
            enabled = quantity > 1,
            contentDescription = stringResource(id = com.mleon.feature.cart.R.string.remove),
            icon = Icons.Filled.Remove
        )
        Text(
            text = quantity.toString(),
            modifier = Modifier.padding(
                horizontal = dimensionResource(id = R.dimen.product_detail_quantity_text_padding)
            )
        )
        QuantityButton(
            onClick = { onQuantityChange(quantity + 1)},
            contentDescription = stringResource(id = com.mleon.feature.cart.R.string.add),
            icon = Icons.Default.Add
        )
    }
}

@Composable
fun AddToCartButton(onAddToCart: () -> Unit) {
    Button(onClick = onAddToCart) {
        Text(text = stringResource(R.string.product_detail_add_to_cart))
    }
}

@Composable
fun IncludesDrinkRow(includesDrink: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.product_detail_corner_radius))
            )
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.product_detail_row_padding))
    ) {
        Icon(
            imageVector = Icons.Default.LocalDrink,
            contentDescription = stringResource(R.string.product_detail_includes_drink_icon),
            modifier = Modifier.padding(
                start = dimensionResource(id = R.dimen.product_detail_icon_start_padding),
                end = dimensionResource(id = R.dimen.product_detail_icon_end_padding)
            )
        )
        Text(
            text = if (includesDrink)
                stringResource(R.string.product_detail_includes_drink)
            else
                stringResource(R.string.product_detail_no_drink),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun CategoryChips(categories: List<Categories>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.product_detail_chip_spacing)),
        modifier = Modifier.fillMaxWidth()
    ) {
        categories.forEach { category ->
            AssistChip(
                onClick = {},
                label = { Text(category.getCategoryName()) },
                modifier = Modifier.padding(end = dimensionResource(id = R.dimen.product_detail_chip_padding)),
                colors = AssistChipDefaults.assistChipColors()
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ProductDetailViewPreview() {
    ProductDetailView(
        product = Product(
            id = "1",
            name = "Producto de Prueba",
            description = "Esta es la descripción del producto de prueba. Es un producto muy interesante y delicioso.",
            price = 25000.0,
            imageUrl = "https://example.com/image.jpg",
            includesDrink = false,
            category = listOf(Categories.SALAD, Categories.PIZZA, Categories.VEGETARIAN)
        ),
        quantity = 1,
        onQuantityChange = {},
        onBack = {}
    )
}
