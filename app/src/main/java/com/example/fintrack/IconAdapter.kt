package com.example.fintrack

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class IconAdapter : ListAdapter<Icon, IconAdapter.IconViewHolder>(IconAdapter) {
    private var onClick: (Icon) -> Unit = {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ico, parent, false)
        return IconViewHolder(view)
    }

    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
        val icon = getItem(position)
        holder.bind(icon, onClick)
    }

    fun onClick(onClick: (Icon) -> Unit) {
        this.onClick = onClick
    }

    class IconViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val imageView =
            view.findViewById<ImageView>(R.id.iv_imagem) // Ajuste o ID conforme sua View
        val imageShape = view.findViewById<ImageView>(R.id.iv_shape)
        fun bind(icon: Icon, onClick: (Icon) -> Unit) {
            // Configure o ImageView com o drawable
            imageView.setImageDrawable(icon.icone)
            // Atualize o estado selecionado do ImageView
            imageShape.isSelected = icon.isSelected
            // Defina o fundo do ImageView como um ShapeDrawable
            imageShape.setBackgroundResource(R.drawable.drawable_icon)
            view.rootView.setOnClickListener {
                onClick.invoke(icon)
            }
        }
    }

    companion object : DiffUtil.ItemCallback<Icon>() {
        override fun areContentsTheSame(oldItem: Icon, newItem: Icon): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: Icon, newItem: Icon): Boolean {
            return oldItem.name == newItem.name && oldItem.icone == newItem.icone && oldItem.isSelected == newItem.isSelected
        }
    }
}
