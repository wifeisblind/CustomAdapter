package com.example.democustomadapter.easyadapter

import com.example.democustomadapter.easyadapter.EasyAdapter.CustomItem


@Suppress("UNCHECKED_CAST")
class EasyAdapterHelper<T>(
        private val adapter: OnSubmitListListener,
        private val currentList: MutableList<Any> = mutableListOf(),
        private val customItems: MutableList<CustomItem> = mutableListOf()
) {

    private val itemCount: Int get() = currentList.size

    fun getItemViewType(position: Int): Int {
        val item = currentList[position]
        return if (item is CustomItem) item.layoutId else NORMAL_TYPE
    }

    fun bindViewHolder(position: Int): Pair<Int, Any> {
        return if (currentList[position] is CustomItem) {
            (0 to currentList[position])
        } else {
            (getNormalPos(position) to currentList[position])
        }
    }

    fun getNormalItem(normalPos: Int): T {
        return currentList.filter { it !is CustomItem }[normalPos] as T
    }

    fun insertCustomItem(customItem: CustomItem) {
        val existedItem = customItems.find { it.getInsertPosition(itemCount + 1) == customItem.getInsertPosition(itemCount + 1) }
        if (existedItem == null) {
            customItems.add(customItem)
            currentList.add(customItem.getInsertPosition(itemCount + 1), customItem)
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

    fun getNormalPos(position: Int): Int {
        val offset = customItems.map { it.getInsertPosition(currentList.size) }.filter { it < position }.size
        return position - offset
    }

    companion object {
        const val NORMAL_TYPE = -1
    }
}