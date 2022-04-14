package com.aradevs.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val image: String,
    val itemCount: Int = 0,
) : Parcelable