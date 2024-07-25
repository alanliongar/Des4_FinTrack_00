package com.example.fintrack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class MonyListAdapter() : ListAdapter<MonyData, MonyListAdapter.MonyViewHolder>(MonyListAdapter) {

    private var onClick: (MonyData) -> Unit = {}
    private var onLongClick: (MonyData) -> Unit = {}
    fun setOnClickListener(onClick: (MonyData) -> Unit) {
        this.onClick = onClick
    }

    fun setOnLongClickListener(onLongClick: (MonyData) -> Unit) {
        this.onLongClick = onLongClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mony, parent, false)
        return MonyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MonyViewHolder, position: Int) {
        val cat = getItem(position)
        holder.bind(cat, onClick, onLongClick)
    }

    class MonyViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val tvValue = view.findViewById<TextView>(R.id.tv_value)
        private val tvName = view.findViewById<TextView>(R.id.tv_name)
        private val resColor = view.findViewById<View>(R.id.color_view)
        fun bind(mony: MonyData, onClick: (MonyData) -> Unit, onLongClick: (MonyData) -> Unit) {
            tvValue.text = mony.value.toString()
            tvName.text = mony.name
            view.setOnClickListener {
                onClick.invoke(mony)
            }
            view.setOnLongClickListener {
                onLongClick.invoke(mony)
                true
            }
            //resColor.setBackgroundColor(R.color.red) -> Aqui devo incluir a cor bonitinha, que é da categoria.
        }
    }

    companion object : DiffUtil.ItemCallback<MonyData>() {
        override fun areItemsTheSame(oldItem: MonyData, newItem: MonyData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MonyData, newItem: MonyData): Boolean {
            return oldItem.name == newItem.name && oldItem.category == newItem.category && oldItem.value == newItem.value
        }

    }
}