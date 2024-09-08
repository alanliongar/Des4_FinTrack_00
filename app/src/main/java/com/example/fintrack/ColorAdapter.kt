package com.example.fintrack

import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ColorAdapter() : ListAdapter<Cor, ColorAdapter.itemViewHolder>(ColorAdapter) {
    private var onClick: (Cor) -> Unit = {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cor, parent, false)
        return itemViewHolder(view)
    }

    override fun onBindViewHolder(holder: itemViewHolder, position: Int) {
        val bol = getItem(position)
        holder.bind(bol, onClick)
    }

    fun onClick(onClick: (Cor) -> Unit) {
        this.onClick = onClick
    }

    class itemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val vieww = view.findViewById<View>(R.id.boll)

        fun bind(cor: Cor, onClick: (Cor) -> Unit) {
            val shapeDrawable = ShapeDrawable(OvalShape())
            shapeDrawable.paint.color = cor.color

            val backgroundDrawable = ContextCompat.getDrawable(itemView.context, R.drawable.drawable_ball)
            val layerDrawable = LayerDrawable(arrayOf(shapeDrawable, backgroundDrawable))
            vieww.background = layerDrawable

            vieww.isSelected = cor.isSelected
            Log.d("alanlion", cor.isSelected.toString())
            vieww.rootView.setOnClickListener {
                onClick.invoke(cor)
            }
        }
    }

    companion object : DiffUtil.ItemCallback<Cor>() {
        override fun areItemsTheSame(oldItem: Cor, newItem: Cor): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Cor, newItem: Cor): Boolean {
            return oldItem.name == newItem.name && oldItem.color == newItem.color
        }
    }
}

