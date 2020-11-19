package com.example.democustomadapter.easyadapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LoadMoreScrollListener(private val listener: OnLoadMoreListener) : RecyclerView.OnScrollListener() {

    private var cursor: Int = 0

    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

        val ll = recyclerView.layoutManager as LinearLayoutManager
        val itemCount = recyclerView.adapter?.itemCount ?: 0

        val current = ll.findLastVisibleItemPosition()
        if (current != cursor
                && current == itemCount - 1) {
            cursor = current
            listener.onLoadMore()
        }
    }
}