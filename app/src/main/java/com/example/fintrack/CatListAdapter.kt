package com.example.fintrack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class CatListAdapter : ListAdapter<CatUiData, CatListAdapter.CatViewHolder>(CatListAdapter) {
    private var onClick: (CatUiData) -> Unit = {}
    private var onLongClick: (CatUiData) -> Unit = {}

    fun setOnClickListener(onClick: (CatUiData) -> Unit) {
        this.onClick = onClick
    }

    fun setOnLongClickListener(onLongClick: (CatUiData) -> Unit) {
        this.onLongClick = onLongClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val vieww =
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CatViewHolder(vieww)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        val cat = getItem(position)
        holder.bind(cat, onClick, onLongClick)
    }

    class CatViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val tvName = view.findViewById<TextView>(R.id.tv_catname)

        fun bind(cat: CatUiData, onClick: (CatUiData) -> Unit, onLongClick: (CatUiData) -> Unit) {
            tvName.text = cat.name
            tvName.isSelected = cat.isSelected
            view.setOnLongClickListener {
                onLongClick.invoke(cat)
                true
            }
            view.setOnClickListener {
                onClick.invoke(cat)
            }
        }
    }
    companion object : DiffUtil.ItemCallback<CatUiData>() {
        override fun areItemsTheSame(oldItem: CatUiData, newItem: CatUiData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CatUiData, newItem: CatUiData): Boolean {
            return oldItem.name == newItem.name
        }
    }
}