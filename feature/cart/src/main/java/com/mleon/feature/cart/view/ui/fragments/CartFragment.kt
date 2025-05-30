package com.mleon.feature.cart.view.ui.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mleon.feature.cart.databinding.FragmentCartBinding
import com.mleon.feature.cart.view.CartItem
import com.mleon.feature.cart.view.ui.adapters.CartAdapter


class CartFragment : Fragment() {
    private lateinit var adapter: CartAdapter

    private var _binding: FragmentCartBinding ? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = CartFragment()
    }

    private val viewModel: CartViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.rvCart?.layoutManager = LinearLayoutManager(requireContext())

        val adapter = CartAdapter(
            emptyList(),
            onAddToCart = { product, qty -> viewModel.addToCart(product) },
            onEditQuantity = { product, qty -> viewModel.editQuantity(product, qty) },
            onDelete = { product -> viewModel.removeFromCart(product) }
        )

        binding.rvCart.adapter = adapter

        Log.d("CartFragment", "onViewCreated: CartFragment created")
        viewModel.cartItems.value?.let { Log.d("CartFragment", it.toString()) }
        // Observe the cart items from the ViewModel
        viewModel.cartItems.observe(viewLifecycleOwner) { itemsMap ->

            Log.d("CartFragment", "Observed cart items: $itemsMap")
            val cartItems = itemsMap.map { (product, quantity) -> CartItem(product, quantity) }
            adapter.updateList(cartItems)
        }

    }
}