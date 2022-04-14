package com.aradevs.taller_practico_2_ma171622_mg171623.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aradevs.domain.Product
import com.aradevs.domain.TransactionType
import com.aradevs.taller_practico_2_ma171622_mg171623.R
import com.aradevs.taller_practico_2_ma171622_mg171623.databinding.MedicineItemBinding
import com.aradevs.taller_practico_2_ma171622_mg171623.utils.bindImage
import com.c3rberuss.androidutils.gone
import com.c3rberuss.androidutils.visible

class MedicineListAdapter(val transactionType: TransactionType, val onTap: (Product) -> Unit) :
    ListAdapter<Product, MedicineListAdapter.ProductViewHolder>(diffUtils) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return MedicineItemBinding.inflate(LayoutInflater.from(parent.context), parent, false).run {
            ProductViewHolder(this)
        }
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProductViewHolder(private val binding: MedicineItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(product: Product) {
            with(binding) {
                name.text = product.name
                image.bindImage(product.image, R.drawable.ic_medicine)
                when (transactionType) {
                    TransactionType.LIST -> {
                        delete.gone()
                        actionContainer.setCardBackgroundColor(binding.root.context.getColor(R.color.accent))
                        price.text = "\$${product.price}"
                        price.visible()
                        root.setOnClickListener {
                            onTap(product)
                        }
                    }
                    TransactionType.HISTORY -> {
                        delete.gone()
                        actionContainer.setCardBackgroundColor(binding.root.context.getColor(R.color.accent))
                        price.text =
                            binding.root.resources.getString(R.string.previously_purchased_template,
                                product.itemCount.toString())
                        price.visible()
                        root.setOnClickListener {
                            onTap(product)
                        }
                    }
                    TransactionType.CART -> {
                        delete.visible()
                        actionContainer.setCardBackgroundColor(binding.root.context.getColor(R.color.white))
                        actionContainer.setOnClickListener {
                            onTap(product)
                        }
                        price.setTextColor(binding.root.context.getColor(R.color.accent))
                    }
                }
            }
        }
    }
}

private val diffUtils = object : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }

}