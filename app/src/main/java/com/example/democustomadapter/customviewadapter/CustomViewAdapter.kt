package com.example.democustomadapter.customviewadapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

@Suppress("UNCHECKED_CAST")
abstract class CustomViewAdapter<T, VH : RecyclerView.ViewHolder>(diffCallback: NormalItemCallback<T>) : ListAdapter<Any, RecyclerView.ViewHolder>(diffCallback) {

    abstract fun onCreateNormalViewHolder(parent: ViewGroup, viewType: Int): VH

    abstract fun onBindNormalViewHolder(holder: VH, position: Int)

    private val customViews: MutableList<CustomItemView> = mutableListOf()

    override fun getItemViewType(position: Int): Int {
        return if (currentList[position] is CustomItemView) {
            customViews.indexOf(currentList[position])
        } else {
            NORMAL_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == NORMAL_ITEM) {
            onCreateNormalViewHolder(parent, viewType)
        } else {
            CustomViewHolder(customViews[viewType].getContainer(parent))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if ((holder is CustomViewHolder).not()) {

            val offset = customViews.map { it.getInsertPosition(itemCount) }.filter { it < position }.size

            onBindNormalViewHolder(holder as VH, position - offset)
        } else {
            customViews[getItemViewType(position)].onViewCreated(holder.itemView)
        }
    }

    override fun getItem(position: Int): T {
        return  currentList.filterNot { it is CustomItemView }[position] as T
    }

    fun insertCustomView(customView: CustomItemView) {
        val target = customViews.find { it.getInsertPosition(itemCount) == customView.getInsertPosition(itemCount) }
        if (target == null) {
            customViews.add(customView)
        } else {
            val index = customViews.indexOf(target)
            customViews[index] = customView
        }
        submitList(processList(currentList))
    }

    fun submitNormalList(list: List<T>) {
        submitList(processList(list as List<Any>))
    }

    private fun processList(list: List<Any>): List<Any> {
        val muList = list.toMutableList()
        for (c in customViews) {
            val insertPos = c.getInsertPosition(list.size + customViews.size)
            if (insertPos > muList.size) {
                muList.add(muList.size, c)
            } else {
                muList.add(c.getInsertPosition(list.size + customViews.size), c)
            }
        }

        return muList
    }

    companion object {
        private const val NORMAL_ITEM = -1
    }

    private class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    @Suppress("UNCHECKED_CAST")
    abstract class NormalItemCallback<T> : DiffUtil.ItemCallback<Any>() {

        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return if (oldItem is CustomItemView || newItem is CustomItemView) {
                oldItem === newItem
            } else {
                areNormalItemsTheSame(oldItem as T, newItem as T)
            }
        }

        abstract fun areNormalItemsTheSame(oldItem: T, newItem: T): Boolean

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return if (oldItem is CustomItemView || newItem is CustomItemView) {
                oldItem === newItem
            } else {
                areNormalContentsTheSame(oldItem as T, newItem as T)
            }
        }

        abstract fun areNormalContentsTheSame(oldItem: T, newItem: T): Boolean
    }

    abstract class CustomItemView(
        private val layoutId: Int
    ) {
        fun getContainer(parent: ViewGroup): View = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)

        abstract fun onViewCreated(view: View)

        abstract fun getInsertPosition(itemCount: Int): Int
    }
}