package com.aradevs.taller_practico_2_ma171622_mg171623.views.shopping_cart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aradevs.domain.Product
import com.aradevs.domain.TransactionType
import com.aradevs.domain.coroutines.Status
import com.aradevs.taller_practico_2_ma171622_mg171623.R
import com.aradevs.taller_practico_2_ma171622_mg171623.adapters.MedicineListAdapter
import com.aradevs.taller_practico_2_ma171622_mg171623.databinding.ShoppingCartFragmentBinding
import com.c3rberuss.androidutils.navigateOff
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingCartFragment : Fragment(R.layout.shopping_cart_fragment) {
    private val binding: ShoppingCartFragmentBinding by viewBinding()
    private val viewModel: ShoppingCartViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeProductStatus()
        viewModel.getShoppingCartItems()
    }


    private fun observeProductStatus() {
        viewModel.productStatus.observe(viewLifecycleOwner) {
            when (it) {
                is Status.Success -> {
                    setupUI(it.data)
                }
                is Status.Error -> {
                    Snackbar.make(binding.root,
                        getString(R.string.error_fetching_data),
                        LENGTH_SHORT).show()
                    binding.placeOrder.isEnabled = false
                }
                is Status.Loading -> {
                    binding.placeOrder.isEnabled = false
                }
                else -> binding.placeOrder.isEnabled = false
            }
        }
    }

    private fun setupUI(items: List<Product>) {
        with(binding) {
            medicinesList.adapter = MedicineListAdapter(TransactionType.CART) {
                deleteProductFromCart(it)
            }.apply { submitList(items) }
            purchaseTotal.text = getString(R.string.amount_to_pay,
                "%.2f".format(items.fold(0.0) { purchaseTotal, itemValue ->
                    purchaseTotal + itemValue.price
                }))
            placeOrder.setOnClickListener {
                placeOrder(items)
            }
            placeOrder.isEnabled = !items.isNullOrEmpty()

            if (items.isNullOrEmpty() && viewModel.transactionStatus.value is Status.Success) {
                Snackbar.make(binding.root,
                    getString(R.string.order_added_sucessfully),
                    LENGTH_SHORT).show()
                navigateOff(R.id.medicine_list_fragment)
            }
        }
    }

    private fun deleteProductFromCart(product: Product) {
        viewModel.deleteProductFromCart(product.id)
    }

    private fun placeOrder(items: List<Product>) {
        viewModel.placeOrder(items)
    }
}