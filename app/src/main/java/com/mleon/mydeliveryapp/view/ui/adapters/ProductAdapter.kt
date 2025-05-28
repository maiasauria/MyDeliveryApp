package com.mleon.mydeliveryapp.view.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mleon.mydeliveryapp.R
import com.mleon.core.model.Product

class ProductAdapter(
    private var products: List<com.mleon.core.model.Product>,
    private val onAddToCart: (com.mleon.core.model.Product, Int) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    // Store quantity per item position
    private val quantities = mutableMapOf<Int, Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        val quantity = quantities[position] ?: 1
        holder.bind(product, quantity)

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
            onAddToCart(product, qty)
        }
    }

    fun updateList(newProducts: List<com.mleon.core.model.Product>) {
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

        fun bind(product: com.mleon.core.model.Product, quantity: Int) {
            tvName.text = product.name
            tvDescription.text = product.description
            tvPrice.text = "$${product.price}"
            tvQuantity.text = quantity.toString()
        }
    }
}