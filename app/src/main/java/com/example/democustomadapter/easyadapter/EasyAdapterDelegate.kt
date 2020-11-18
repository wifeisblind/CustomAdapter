package com.example.democustomadapter.easyadapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.democustomadapter.easyadapter.EasyAdapter.Companion.NO_TYPE
import com.example.democustomadapter.easyadapter.EasyAdapter.CustomItemView

abstract class EasyAdapterDelegate <T, VH : EasyViewHolder> {

    abstract fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH

    /**
     * Negative value is reserved, please using value larger than 0
     */
    open fun getItemViewType(position: Int): Int = NO_TYPE

    open fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        realAdapter.onBindViewHolder(holder, position)
    }

    abstract fun onBindViewHolder(holder: VH, position: Int)

    abstract fun areItemsTheSame(oldItem: T, newItem: T): Boolean

    abstract fun areContentsTheSame(oldItem: T, newItem: T): Boolean

    open fun getChangePayload(oldItem: T, newItem: T): Any? {
        return null
    }

    @Suppress("UNCHECKED_CAST")
    val diffCallback: DiffUtil.ItemCallback<Any> = object : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return if (oldItem is CustomItemView || newItem is CustomItemView) {
                oldItem === newItem
            } else {
                this@EasyAdapterDelegate.areItemsTheSame(oldItem as T, newItem as T)
            }
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return if (oldItem is CustomItemView || newItem is CustomItemView) {
                oldItem === newItem
            } else {
                this@EasyAdapterDelegate.areContentsTheSame(oldItem as T, newItem as T)
            }
        }

        override fun getChangePayload(oldItem: Any, newItem: Any): Any? {
            return getChangePayload(oldItem as T, newItem as T)
        }
    }

    private val realAdapter: EasyAdapter<T, VH> = EasyAdapter(this)

    open val setting: RecyclerView.() -> Unit = {
        layoutManager = LinearLayoutManager(context)
        adapter = realAdapter
    }

    open val twoSpanSetting: RecyclerView.() -> Unit = {
        layoutManager = GridLayoutManager(context, 2).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if ( realAdapter.isCustomType(position)) 1 else 2
                }
            }
        }
        adapter = realAdapter
    }

    protected fun getItem(position: Int): T = realAdapter.getNormalItem(position)

    fun submitList(list: List<T>) {
        realAdapter.submitNormalList(list)
    }

    fun insertCustomView(customView: CustomItemView) {
        realAdapter.insertCustomView(customView)
    }
}