package com.example.fintrack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MonyListAdapter(val catDao: CatDao, val lifecycleOwner: LifecycleOwner) :
    ListAdapter<MonyUiData, MonyListAdapter.MonyViewHolder>(MonyListAdapter) {

    private var onClick: (MonyUiData) -> Unit = {}
    private var onLongClick: (MonyUiData) -> Unit = {}
    fun setOnClickListener(onClick: (MonyUiData) -> Unit) {
        this.onClick = onClick
    }

    fun setOnLongClickListener(onLongClick: (MonyUiData) -> Unit) {
        this.onLongClick = onLongClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mony, parent, false)
        return MonyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MonyViewHolder, position: Int) {
        val cat = getItem(position)
        holder.bind(cat, onClick, onLongClick, catDao, lifecycleOwner)
    }

    class MonyViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val tvValue = view.findViewById<TextView>(R.id.tv_value)
        private val tvName = view.findViewById<TextView>(R.id.tv_name)
        private val resColor = view.findViewById<View>(R.id.color_view)
        private val iconView = view.findViewById<ImageView>(R.id.iv_icon)


        fun bind(
            mony: MonyUiData,
            onClick: (MonyUiData) -> Unit,
            onLongClick: (MonyUiData) -> Unit,
            catDao: CatDao,
            lifecycleOwner: LifecycleOwner
        ) {
            tvValue.text = mony.value.toString()
            tvName.text = mony.name

            lifecycleOwner.lifecycleScope.launch {
                val categoria: CatEntity =
                    withContext(Dispatchers.IO) { catDao.findById(mony.category) }
                withContext(Dispatchers.Main) {
                    resColor.setBackgroundColor(categoria.color)
                    iconView.setImageResource(categoria.icon)
                }
            }

            view.rootView.setOnClickListener {
                onClick.invoke(mony)
            }
            view.rootView.setOnLongClickListener {
                onLongClick.invoke(mony)
                true
            }
        }
    }

    companion object : DiffUtil.ItemCallback<MonyUiData>() {
        override fun areItemsTheSame(oldItem: MonyUiData, newItem: MonyUiData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MonyUiData, newItem: MonyUiData): Boolean {
            return oldItem.name == newItem.name && oldItem.category == newItem.category && oldItem.value == newItem.value
        }
    }
}