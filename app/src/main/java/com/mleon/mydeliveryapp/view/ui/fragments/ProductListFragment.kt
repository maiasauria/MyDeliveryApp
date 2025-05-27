package com.mleon.mydeliveryapp.view.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mleon.mydeliveryapp.R
import com.mleon.mydeliveryapp.view.ui.adapters.ProductAdapter
import com.mleon.mydeliveryapp.data.model.Product
import com.mleon.mydeliveryapp.databinding.FragmentProductListBinding
import com.mleon.mydeliveryapp.view.ui.viewmodels.ProductListViewModel


class ProductListFragment : Fragment() {



    private lateinit var adapter: ProductAdapter

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: ProductListViewModel by viewModels()

        val sharedPref = requireContext().getSharedPreferences("preferencias", 0)

        viewModel.loadUserEmail(sharedPref)

        viewModel.userEmail.observe(viewLifecycleOwner) { email ->
            binding.tvUserName.text = "Hola, $email!"
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvProducts)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = ProductAdapter(emptyList()) { product: Product, quantity: Int ->
            //
        }
        recyclerView.adapter = adapter

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ProductListViewModel.ProductListUiState.Loading -> {
                    //
                }
                is ProductListViewModel.ProductListUiState.Success -> {
                    adapter.updateList(state.products)
                }
                is ProductListViewModel.ProductListUiState.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.reloadProducts()

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onSearchTextChanged(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): ProductListFragment {
            return ProductListFragment()
        }
    }

}