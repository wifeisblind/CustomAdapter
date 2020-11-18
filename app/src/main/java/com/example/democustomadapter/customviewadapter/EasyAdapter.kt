package com.example.democustomadapter.customviewadapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.democustomadapter.customviewadapter.EasyAdapterHelper.Companion.NORMAL_TYPE

@Suppress("UNCHECKED_CAST")
class EasyAdapter<T, VH : EasyViewHolder>(
    private val delegate: EasyAdapterDelegate<T, VH>
) : ListAdapter<Any, RecyclerView.ViewHolder>(delegate.diffCallback), OnSubmitListListener {

    override fun commitList(list: MutableList<Any>) {
        Log.d("EasyAdapter", list.toString())
        submitList(list)
    }

    private val helper: EasyAdapterHelper<T> = EasyAdapterHelper(this)

    override fun getItemViewType(position: Int): Int {
        return when (val type = helper.getItemViewType(position)) {
            NORMAL_TYPE -> delegate.getItemViewType(position)
            else -> type.unaryMinus()
        }
    }

    fun isCustomType(position: Int): Boolean = getItemViewType(position) < 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when {
            viewType < 0 -> {
                CustomViewHolder(LayoutInflater.from(parent.context).inflate(viewType.unaryMinus(), parent, false))
            }
            else -> delegate.onCreateViewHolder(parent, viewType).apply {
                helper = this@EasyAdapter.helper
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        when(holder) {
            is CustomViewHolder -> super.onBindViewHolder(holder, position, payloads)
            else -> delegate.onBindViewHolder(holder as VH, position, payloads)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val (normalPos, item) = helper.bindViewHolder(position)
        when(holder) {
            is CustomViewHolder -> (item as CustomItemView).onViewCreated(holder.itemView)
            else -> delegate.onBindViewHolder(holder as VH, normalPos)
        }
    }

    fun getNormalItem(position: Int): T = helper.getNormalItem(position)

    fun insertCustomView(customView: CustomItemView) = helper.insertCustomItem(customView)

    fun submitNormalList(list: List<T>) {
        helper.submitNormalList(list)
    }

    private class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface CustomItem {
        val layoutId: Int
        fun getInsertPosition(itemCount: Int): Int
    }

    abstract class CustomItemView(override val layoutId: Int) : CustomItem {

        abstract fun onViewCreated(view: View)
    }

    companion object {
        const val NO_TYPE = 0
    }
}