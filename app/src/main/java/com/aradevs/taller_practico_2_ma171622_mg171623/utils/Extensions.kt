package com.aradevs.taller_practico_2_ma171622_mg171623.utils

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide

fun ImageView.bindImage(
    image: String?, @DrawableRes placeholder: Int,
) {
    if (image.isNullOrBlank()) {
        setImageResource(placeholder)
        return
    }
    try {
        Glide.with(context)
            .load(image)
            .run {
                error(placeholder)
            }
            .placeholder(placeholder)
            .into(this)
    } catch (e: Exception) {
        setImageResource(placeholder)
    }
}