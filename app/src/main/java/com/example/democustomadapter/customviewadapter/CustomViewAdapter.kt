package com.example.democustomadapter.customviewadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

@Suppress("UNCHECKED_CAST")
class CustomViewAdapter<T, VH : RecyclerView.ViewHolder>(
    private val delegate: EasyAdapter<T, VH>
) : ListAdapter<Any, RecyclerView.ViewHolder>(delegate.diffCallback), CustomViewAdapterHelperDelegate {

    override fun commitList(list: MutableList<Any>) {
        submitList(list)
    }

    private val helper: CustomViewAdapterHelper<T> = CustomViewAdapterHelper(this)

    override fun getItemViewType(position: Int): Int {
        return when (val type = helper.getItemViewType(position)) {
            -1 -> delegate.getItemViewType(position)
            else -> type.unaryMinus()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when {
            viewType < 0 -> {
                CustomViewHolder(LayoutInflater.from(parent.context).inflate(viewType.unaryMinus(), parent, false))
            }
            else -> delegate.onCreateViewHolder(parent, viewType)
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