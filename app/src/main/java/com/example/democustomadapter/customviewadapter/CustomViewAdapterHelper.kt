package com.example.democustomadapter.customviewadapter

import com.example.democustomadapter.customviewadapter.CustomViewAdapter.Companion.VIEW_TYPE_NORMAL
import com.example.democustomadapter.customviewadapter.CustomViewAdapter.CustomItem
import com.example.democustomadapter.customviewadapter.CustomViewAdapterHelper.ViewHolderType.CustomType
import com.example.democustomadapter.customviewadapter.CustomViewAdapterHelper.ViewHolderType.NormalType


@Suppress("UNCHECKED_CAST")
class CustomViewAdapterHelper<T>(
        private val adapter: CustomViewAdapterHelperDelegate,
        private val currentList: MutableList<Any> = mutableListOf(),
        private val customItems: MutableList<CustomItem> = mutableListOf()
) {

    private val itemCount: Int get() = currentList.size

    fun getItemViewType(position: Int): Int {
        val item = currentList[position]
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
        if (currentList[position] !is CustomItem) {
            val offset = customItems.map { it.getInsertPosition(currentList.size) }.filter { it < position }.size
            bindNormalViewHolder(position - offset)
        }
    }

    fun getNormalItem(normalPos: Int): T {
        return currentList.filter { it !is CustomItem }[normalPos] as T
    }

    fun insertCustomItem(customItem: CustomItem) {
        val existedItem = customItems.find { it.getInsertPosition(itemCount) == customItem.getInsertPosition(itemCount) }
        if (existedItem == null) {
            customItems.add(customItem)
            currentList.add(customItem)
        } else {
            val index = customItems.indexOf(existedItem)
            customItems[index] = customItem
            currentList[customItem.getInsertPosition(itemCount)] = customItem
        }
        adapter.commitList(currentList.toMutableList())
    }

    fun submitNormalList(list: List<T>) {
        currentList.clear()
        currentList.addAll(list as List<Any>)
        for (c in customItems) {
            val insertPos = c.getInsertPosition(list.size + customItems.size)
            currentList.add(insertPos, c)
        }
        adapter.commitList(currentList.toMutableList())
    }

    sealed class ViewHolderType {
        object NormalType : ViewHolderType()
        data class CustomType(val customItem: CustomItem) : ViewHolderType()
    }
}