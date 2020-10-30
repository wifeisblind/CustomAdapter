package com.example.democustomadapter.customviewadapter

import com.example.democustomadapter.customviewadapter.CustomViewAdapter.CustomItem
import com.example.democustomadapter.customviewadapter.CustomViewAdapterHelper.ViewHolderType.CustomType
import com.example.democustomadapter.customviewadapter.CustomViewAdapterHelper.ViewHolderType.NormalType


@Suppress("UNCHECKED_CAST")
class CustomViewAdapterHelper<T>(private val adapter: CustomViewAdapterHelperDelegate) {

    private val customItems: MutableList<CustomItem> = mutableListOf()

    fun getItemViewType(position: Int): Int {
        val item = adapter.currentList[position]
        return if (item is CustomItem) {
            customItems.indexOf(item)
        } else {
            VIEW_TYPE_NORMAL
        }
    }

    fun createViewHolder(viewType: Int): ViewHolderType {
        return if (viewType == VIEW_TYPE_NORMAL) {
            NormalType
        } else {
            CustomType(customItems[viewType])
        }
    }

    fun onBindViewHolder(position: Int, bindNormalViewHolder: (normalPos: Int) -> Unit) {
        if (adapter.currentList[position] !is CustomItem) {
            bindNormalViewHolder(position)
        }
    }

    fun getNormalItem(normalPos: Int): T {
        return adapter.currentList[normalPos] as T
    }

    fun insertCustomItem(customItem: CustomItem) {
        customItems.add(customItem)
        adapter.currentList.add(customItem)
        adapter.submitList(adapter.currentList)
    }

    fun submitNormalList(list: List<T>) {
        val muList = list.toMutableList() as MutableList<Any>
        for (c in customItems) {
            val insertPos = c.getInsertPosition(list.size + customItems.size)
//            if (insertPos > muList.size) {
//                muList.add(muList.size, c)
//            } else {
            muList.add(insertPos, c)
//            }
        }
        adapter.submitList(muList)
    }

    sealed class ViewHolderType {
        object NormalType : ViewHolderType()
        data class CustomType(val customItem: CustomItem) : ViewHolderType()
    }

    companion object {
        private const val VIEW_TYPE_NORMAL = -1
    }
}