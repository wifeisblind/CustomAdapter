package com.example.democustomadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class CustomItemView(
    private val layoutId: Int
) {
    fun getContainer(parent: ViewGroup): View = LayoutInflater.from(parent.context).inflate(layoutId, parent, false).also {
        onViewCreated(it)
    }

    abstract fun onViewCreated(view: View)

    abstract fun getInsertPosition(itemCount: Int): Int
}

@Suppress("UNCHECKED_CAST")
abstract class CustomViewAdapter<T, VH : RecyclerView.ViewHolder>(diffCallback: NormalItemCallback<T>) : ListAdapter<Any, RecyclerView.ViewHolder>(diffCallback) {

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
        }
    }

    override fun getItem(position: Int): T {
        return  currentList.filterNot { it is CustomItemView }[position] as T
    }

    abstract fun onCreateNormalViewHolder(parent: ViewGroup, viewType: Int): VH

    abstract fun onBindNormalViewHolder(holder: VH, position: Int)

    fun insertCustomView(customView: CustomItemView) {
        customViews.add(customView)
    }

    fun submitNormalList(list: List<T>) {
        val muList = list.toMutableList() as MutableList<Any>
        for (c in customViews) {
            val insertPos = c.getInsertPosition(list.size + customViews.size)
            if (insertPos > muList.size) {
                muList.add(muList.size, c)
            } else {
                muList.add(c.getInsertPosition(list.size + customViews.size), c)
            }
        }

        submitList(muList)
    }

    companion object {
        private const val NORMAL_ITEM = -1
    }

    private class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    @Suppress("UNCHECKED_CAST")
    abstract class NormalItemCallback<T> : DiffUtil.ItemCallback<Any>() {

        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return if (oldItem is CustomItemView || newItem is CustomItemView) true else areNormalItemsTheSame(oldItem as T, newItem as T)
        }

        abstract fun areNormalItemsTheSame(oldItem: T, newItem: T): Boolean

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return if (oldItem is CustomItemView || newItem is CustomItemView) true else areNormalContentsTheSame(oldItem as T, newItem as T)
        }

        abstract fun areNormalContentsTheSame(oldItem: T, newItem: T): Boolean
    }
}