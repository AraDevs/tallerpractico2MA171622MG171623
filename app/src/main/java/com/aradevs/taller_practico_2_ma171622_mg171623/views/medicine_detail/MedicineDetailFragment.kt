package com.aradevs.taller_practico_2_ma171622_mg171623.views.medicine_detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aradevs.domain.coroutines.Status
import com.aradevs.domain.exceptions.ItemAlreadyInCartException
import com.aradevs.taller_practico_2_ma171622_mg171623.R
import com.aradevs.taller_practico_2_ma171622_mg171623.databinding.MedicineDetailFragmentBinding
import com.aradevs.taller_practico_2_ma171622_mg171623.utils.bindImage
import com.c3rberuss.androidutils.navigateOff
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MedicineDetailFragment : Fragment(R.layout.medicine_detail_fragment) {
    private val binding: MedicineDetailFragmentBinding by viewBinding()
    private val args: MedicineDetailFragmentArgs by navArgs()
    private val viewModel: MedicineDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeSavingStatus()
        setupUI()
    }

    @SuppressLint("SetTextI18n")
    private fun setupUI() {
        with(args.medicine) {
            binding.image.bindImage(image, R.drawable.ic_medicine)
            binding.name.text = name
            binding.description.text = description
            binding.price.text = "\$${price}"
            binding.backButton.setOnClickListener {
                navigateOff(R.id.medicine_list_fragment)
            }
            binding.addToCart.setOnClickListener {
                viewModel.saveItemToCart(args.medicine.id)
            }
        }
    }

    private fun observeSavingStatus() {
        viewModel.savingStatus.observe(viewLifecycleOwner) {
            when (it) {
                is Status.Success -> {
                    Snackbar.make(binding.root, getString(R.string.successfully_added_to_cart), LENGTH_SHORT)
                        .show()
                    navigateOff(R.id.medicine_list_fragment)
                }
                is Status.Error -> {
                    binding.addToCart.isEnabled = true
                    if (it.exception is ItemAlreadyInCartException) {
                        Snackbar.make(binding.root,
                            getString(R.string.product_already_in_cart),
                            LENGTH_SHORT)
                            .show()
                    }
                }
                is Status.Loading -> {
                    binding.addToCart.isEnabled = false
                }
                is Status.Initial -> {
                    binding.addToCart.isEnabled = true
                }
            }
        }
    }
}