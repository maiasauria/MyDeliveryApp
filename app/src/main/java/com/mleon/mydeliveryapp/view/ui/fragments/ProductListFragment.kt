package com.mleon.mydeliveryapp.view.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mleon.mydeliveryapp.R
import com.mleon.mydeliveryapp.view.ui.adapters.ProductAdapter
import com.mleon.mydeliveryapp.data.model.Product
import com.mleon.mydeliveryapp.view.ui.viewmodels.ProductListViewModel


class ProductListFragment : Fragment() {

    private val viewModel: ProductListViewModel by viewModels()
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvProducts)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = ProductAdapter(emptyList()) { product: Product, quantity: Int ->
            // Handle add to cart action here
        }
        recyclerView.adapter = adapter

        viewModel.products.observe(viewLifecycleOwner) { products ->
            adapter.updateList(products)
        }

        viewModel.reloadProducts()

        val etSearch = view.findViewById<EditText>(R.id.etSearch)
        val btnSearch = view.findViewById<Button>(R.id.btnBuscar)

        btnSearch.setOnClickListener {
            val query = etSearch.text.toString()
            if (query.isEmpty()) {
                viewModel.reloadProducts()
            } else {
                viewModel.searchProducts(query)
            }
        }
    }

    companion object {
        fun newInstance(): ProductListFragment {
            return ProductListFragment()
        }
    }

}