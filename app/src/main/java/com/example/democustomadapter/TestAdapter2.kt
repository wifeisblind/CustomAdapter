package com.example.democustomadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_footer.view.*
import kotlinx.android.synthetic.main.layout_item.view.*
import java.lang.IllegalStateException

class TestAdapter2 : ListAdapter<ItemData, RecyclerView.ViewHolder>(diffUtil) {

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is ItemData.Normal -> R.layout.layout_item
            is ItemData.Footer -> R.layout.layout_footer
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            R.layout.layout_item -> ViewHolder(parent)
            R.layout.layout_footer -> Footer(parent)
            else -> throw IllegalStateException("")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ViewHolder -> holder.bind(getItem(position) as ItemData.Normal)
            is Footer -> holder.bind(getItem(position) as ItemData.Footer)
        }
    }

    class ViewHolder(
            parent: ViewGroup
    ) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_item, parent, false)
    ) {
        fun bind(data: ItemData.Normal) {
            itemView.tvTitle.text = data.value
        }
    }

    class Footer(
            parent: ViewGroup
    ) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_footer, parent, false)
    ) {
        fun bind(data: ItemData.Footer) {
            itemView.layMore.visibility = if (data.isLoading) {
                View.VISIBLE
            } else {
                View.GONE
            }
            itemView.layEnd.visibility = if (data.isLoading) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }
}

val diffUtil = object : DiffUtil.ItemCallback<ItemData>() {
    override fun areItemsTheSame(oldItem: ItemData, newItem: ItemData): Boolean {
        return if (oldItem is ItemData.Normal && newItem is ItemData.Normal) {
            oldItem.value == newItem.value
        } else {
            false
        }
    }

    override fun areContentsTheSame(oldItem: ItemData, newItem: ItemData): Boolean {
        return (oldItem as ItemData.Normal) == (newItem as ItemData.Normal)
    }
}

sealed class ItemData {
    data class Normal(val value: String) : ItemData()
    data class Footer(val isLoading: Boolean) : ItemData()
}