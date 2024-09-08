package com.example.fintrack

import android.graphics.drawable.Drawable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CatEntity(
    @PrimaryKey
    @ColumnInfo("key")
    val name: String,
    @ColumnInfo("color")
    val color: Int,
    @ColumnInfo("icon")
    val icon: Int,
    @ColumnInfo("is_selected")
    val isSelected: Boolean
)
