package com.example.democustomadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.democustomadapter.DemoAdapter.DemoHolder
import com.example.democustomadapter.easyadapter.EasyAdapterDelegate
import com.example.democustomadapter.easyadapter.EasyViewHolder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.layout_item.*

class DemoAdapter(private val listener: DemoHolderClickListener) : EasyAdapterDelegate<ItemData, DemoHolder>() {

    interface DemoHolderClickListener {
        fun onAddFavorite(position: Int)
        fun onDelete(position: Int)
    }

    override fun onBindViewHolder(holder: DemoHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            holder.bindFavorite(payloads[0] as Boolean)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DemoHolder {
        return DemoHolder(parent, listener)
    }

    override fun onBindViewHolder(holder: DemoHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun areItemsTheSame(oldItem: ItemData, newItem: ItemData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ItemData, newItem: ItemData): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: ItemData, newItem: ItemData): Any? {
        return if (oldItem == newItem.copy(isFavorite = newItem.isFavorite.not())) {
            newItem.isFavorite
        } else {
            null
        }
    }

    class DemoHolder(
        parent: ViewGroup,
        private val listener: DemoHolderClickListener
    ) : EasyViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.layout_item, parent, false)
    ) {

        private val context: Context = itemView.context

        init {
            itemView.setOnClickListener {
                Toast.makeText(context, "position: $normalPosition", LENGTH_SHORT).show()
            }

            imgFavorite.setOnClickListener {
                listener.onAddFavorite(normalPosition)
            }

            imgDelete.setOnClickListener {
                listener.onDelete(normalPosition)
            }
        }

        fun bind(data: ItemData) = with(data) {

            tvTitle.text = title
            Glide.with(itemView.context)
                    .load(imgCommodityUrl)
                    .into(imgCommodity)
            tvPrice.text = "$$price"
            bindFavorite(isFavorite)
        }

        fun bindFavorite(isFavorite: Boolean) {
            val img = if (isFavorite) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_outline_favorite_border_24
            imgFavorite.setImageResource(img)
        }
    }
}