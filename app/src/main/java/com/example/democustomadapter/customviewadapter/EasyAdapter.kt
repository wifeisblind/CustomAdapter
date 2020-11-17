package com.example.democustomadapter.customviewadapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.democustomadapter.customviewadapter.CustomViewAdapter.Companion.VIEW_TYPE_NORMAL
import com.example.democustomadapter.customviewadapter.CustomViewAdapter.CustomItemView

abstract class EasyAdapter <T, VH : RecyclerView.ViewHolder> {

    abstract fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH

    open fun getItemViewType(position: Int): Int = VIEW_TYPE_NORMAL

    abstract fun onBindViewHolder(holder: VH, position: Int)

    abstract fun areItemsTheSame(oldItem: T, newItem: T): Boolean

    abstract fun areContentsTheSame(oldItem: T, newItem: T): Boolean

    open fun getChangePayload(oldItem: T, newItem: T): T? {
        return null
    }

    @Suppress("UNCHECKED_CAST")
    val diffCallback: DiffUtil.ItemCallback<Any> = object : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return if (oldItem is CustomItemView || newItem is CustomItemView) {
                oldItem === newItem
            } else {
                this@EasyAdapter.areItemsTheSame(oldItem as T, newItem as T)
            }
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return if (oldItem is CustomItemView || newItem is CustomItemView) {
                oldItem === newItem
            } else {
                this@EasyAdapter.areContentsTheSame(oldItem as T, newItem as T)
            }
        }

        override fun getChangePayload(oldItem: Any, newItem: Any): Any? {
            return getChangePayload(oldItem as T, newItem as T)
        }
    }

    val realAdapter: CustomViewAdapter<T, VH> = CustomViewAdapter(this)

    protected fun getItem(position: Int): T = realAdapter.getNormalItem(position)

    fun submitList(list: List<T>) {
        realAdapter.submitNormalList(list)
    }

    fun insertCustomView(customView: CustomItemView) {
        realAdapter.insertCustomView(customView)
    }
}

val EasyAdapter<*, *>.setting : RecyclerView.() -> Unit get() = {
    layoutManager = LinearLayoutManager(context)
    adapter = this@setting.realAdapter
}