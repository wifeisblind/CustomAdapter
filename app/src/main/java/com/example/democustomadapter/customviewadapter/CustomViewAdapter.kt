package com.example.democustomadapter.customviewadapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.democustomadapter.customviewadapter.CustomViewAdapterHelper.ViewHolderType.*

@Suppress("UNCHECKED_CAST")
class CustomViewAdapter<T, VH : RecyclerView.ViewHolder>(
    private val delegate: EasyAdapter<T, VH>
) : ListAdapter<Any, RecyclerView.ViewHolder>(delegate.diffCallback), CustomViewAdapterHelperDelegate {

    override fun commitList(list: MutableList<Any>) {
        submitList(list)
    }

    private val helper: CustomViewAdapterHelper<T> = CustomViewAdapterHelper(this)

    override fun getItemViewType(position: Int): Int = helper.getItemViewType(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(val type = helper.createViewHolder(viewType)) {
            is NormalType -> delegate.onCreateViewHolder(parent, viewType)
            is CustomType -> createCustomViewHolder(parent, type)
        }
    }

    private fun createCustomViewHolder(parent: ViewGroup, type: CustomType): CustomViewHolder {
        return LayoutInflater.from(parent.context).inflate(type.customItem.layoutId, parent, false).let {
            (type.customItem as CustomItemView).onViewCreated(it)
            CustomViewHolder(it)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        helper.onBindViewHolder(position) { normalPos ->
            delegate.onBindViewHolder(holder as VH, normalPos)
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
        const val VIEW_TYPE_NORMAL = -1
    }
}