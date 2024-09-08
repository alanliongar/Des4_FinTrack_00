package com.example.fintrack

import android.graphics.drawable.Drawable

data class CatUiData(
    val name: String,
    val color: Int,
    val icon: Drawable,
    val isSelected: Boolean
)