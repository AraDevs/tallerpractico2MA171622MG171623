package com.aradevs.taller_practico_2_ma171622_mg171623.views.purchase_history

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
import com.aradevs.taller_practico_2_ma171622_mg171623.databinding.PurchaseHistoryFragmentBinding
import com.c3rberuss.androidutils.navigate
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PurchaseHistoryFragment : Fragment(R.layout.purchase_history_fragment) {
    private val binding: PurchaseHistoryFragmentBinding by viewBinding()
    private val viewModel: PurchaseHistoryViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getPurchases()
        observeMedicineStatus()
    }


    private fun observeMedicineStatus() {
        viewModel.productStatus.observe(viewLifecycleOwner) {
            when (it) {
                is Status.Success -> {
                    setupUI(it.data)
                }
                is Status.Error -> {
                    Snackbar.make(binding.root, getString(R.string.error_fetching_data),
                        BaseTransientBottomBar.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    //do nothing
                }
            }
        }
    }

    private fun setupUI(items: List<Product>) {
        with(binding) {
            medicinesList.adapter = MedicineListAdapter(TransactionType.HISTORY) {
                goToMedicineDetail(it)
            }.apply {
                submitList(items.map { it.copy(itemCount = items.filter { product -> product.id == it.id }.size) }
                    .toSet().toList())
            }
        }
    }

    private fun goToMedicineDetail(product: Product) {
        navigate(PurchaseHistoryFragmentDirections.actionHistoryListToDetail(product))
    }
}