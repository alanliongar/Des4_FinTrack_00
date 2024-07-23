package com.example.fintrack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class CatListAdapter : ListAdapter<CatData, CatListAdapter.CatViewHolder>(CatListAdapter) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val vieww =
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CatViewHolder(vieww)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        val cat = getItem(position)
        holder.bind(cat)
    }

    class CatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvName = view.findViewById<TextView>(R.id.tv_catname)
        //private val catColor = view.findViewById<View>(R.id.)

        fun bind(cat: CatData) {
            tvName.text = cat.name
        }
    }

    companion object : DiffUtil.ItemCallback<CatData>() {
        override fun areItemsTheSame(oldItem: CatData, newItem: CatData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CatData, newItem: CatData): Boolean {
            return oldItem.name == newItem.name
        }

    }

}