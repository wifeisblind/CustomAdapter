package com.example.democustomadapter.customviewadapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.democustomadapter.customviewadapter.CustomViewAdapterHelper.ViewHolderType.*

@Suppress("UNCHECKED_CAST")
abstract class CustomViewAdapter<T, VH : RecyclerView.ViewHolder>(
    diffCallback: NormalItemCallback<T>
) : ListAdapter<Any, RecyclerView.ViewHolder>(diffCallback) {

    abstract fun onCreateNormalViewHolder(parent: ViewGroup, viewType: Int): VH

    abstract fun onBindNormalViewHolder(holder: VH, position: Int)

    private val delegate: CustomViewAdapterHelperDelegate = object : CustomViewAdapterHelperDelegate {
        override val currentList: MutableList<Any> get() = this@CustomViewAdapter.currentList
        override fun submitList(list: MutableList<Any>) {
            this@CustomViewAdapter.submitList(list)
        }
    }

    private val helper: CustomViewAdapterHelper<T> = CustomViewAdapterHelper(delegate)

    override fun getItemViewType(position: Int): Int {
        return helper.getItemViewType(position)
//        return if (currentList[position] is CustomItemView) {
//            customViews.indexOf(currentList[position])
//        } else {
//            NORMAL_ITEM
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(val type = helper.createViewHolder(viewType)) {
            is NormalType -> onCreateNormalViewHolder(parent, viewType)
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
            onBindNormalViewHolder(holder as VH, normalPos)
        }
//
//        if ((holder is CustomViewHolder).not()) {
//
//            val offset = customViews.map { it.getInsertPosition(itemCount) }.filter { it < position }.size
//
//            onBindNormalViewHolder(holder as VH, position - offset)
//        } else {
//            customViews[getItemViewType(position)].onViewCreated(holder.itemView)
//        }
    }

    override fun getItem(position: Int): T = helper.getNormalItem(position)
//        return  currentList.filterNot { it is CustomItemView }[position] as T

    fun insertCustomView(customView: CustomItemView) {
//        val target = customViews.find { it.getInsertPosition(itemCount) == customView.getInsertPosition(itemCount) }
//        if (target == null) {
//            customViews.add(customView)
//        } else {
//            val index = customViews.indexOf(target)
//            customViews[index] = customView
//        }
//        submitList(processList(currentList))

        helper.insertCustomItem(customView)
    }

    fun submitNormalList(list: List<T>) {
        helper.submitNormalList(list)
    }

//    private fun processList(list: List<Any>): List<Any> {
//        val muList = list.toMutableList()
//        for (c in customViews) {
//            val insertPos = c.getInsertPosition(list.size + customViews.size)
//            if (insertPos > muList.size) {
//                muList.add(muList.size, c)
//            } else {
//                muList.add(c.getInsertPosition(list.size + customViews.size), c)
//            }
//        }
//
//        return muList
//    }

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

    interface CustomItem {
        val layoutId: Int
        fun getInsertPosition(itemCount: Int): Int
    }

    abstract class CustomItemView(override val layoutId: Int) : CustomItem {

        abstract fun onViewCreated(view: View)
    }
}