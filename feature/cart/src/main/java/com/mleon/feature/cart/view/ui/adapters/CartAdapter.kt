package com.mleon.feature.cart.view.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mleon.core.model.Product
import com.mleon.feature.cart.R
import com.mleon.feature.cart.view.CartItem

class CartAdapter(
    private var products: List<CartItem>,
    private val onAddToCart: (Product, Int) -> Unit,
    private val onEditQuantity: (Product, Int) -> Unit,
    private val onDelete: (Product) -> Unit
) : RecyclerView.Adapter<CartAdapter.ProductViewHolder>() {

    // Store quantity per item position
    private val quantities = mutableMapOf<Int, Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val cartItem = products[position]
        holder.bind(cartItem.product, cartItem.quantity)

        holder.btnIncrease.setOnClickListener {
            val newQuantity = (quantities[position] ?: 1) + 1
            quantities[position] = newQuantity
            holder.tvQuantity.text = newQuantity.toString()
        }

        holder.btnDecrease.setOnClickListener {
            val current = quantities[position] ?: 1
            if (current > 1) {
                val newQuantity = current - 1
                quantities[position] = newQuantity
                holder.tvQuantity.text = newQuantity.toString()
            }
        }

        holder.btnAddToCart.setOnClickListener {
            val qty = quantities[position] ?: 1
            onAddToCart(cartItem.product, qty)
        }
    }

    fun updateList(newProducts: List<CartItem>) {
        products = newProducts
        quantities.clear()
        notifyDataSetChanged()
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvProductName)
        val tvDescription: TextView = itemView.findViewById(R.id.tvProductDescription)
        val tvPrice: TextView = itemView.findViewById(R.id.tvProductPrice)
        val tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        val btnIncrease: Button = itemView.findViewById(R.id.btnIncrease)
        val btnDecrease: Button = itemView.findViewById(R.id.btnDecrease)
        val btnAddToCart: Button = itemView.findViewById(R.id.btnAddToCart)

        fun bind(product: Product, quantity: Int) {
            tvName.text = product.name
            tvDescription.text = product.description
            tvPrice.text = "$${product.price}"
            tvQuantity.text = quantity.toString()
        }
    }
}