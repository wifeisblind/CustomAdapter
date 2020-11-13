package com.example.democustomadapter.customviewadapter

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView


class Footer(
    @LayoutRes private val layoutRes: Int,
    private val onBindView: View.() -> Unit
) : CustomViewAdapter.CustomItemView(layoutRes) {

    override fun onViewCreated(view: View) {
        onBindView(view)
    }

    override fun getInsertPosition(itemCount: Int): Int = itemCount - 1
}

class Header(
    @LayoutRes private val layoutRes: Int,
    private val onBindView: View.() -> Unit
) : CustomViewAdapter.CustomItemView(layoutRes) {

    override fun onViewCreated(view: View) {
        onBindView(view)
    }

    override fun getInsertPosition(itemCount: Int): Int = 0
}


fun CustomViewAdapter<*, out RecyclerView.ViewHolder>.insertFooter(
    @LayoutRes layoutRes: Int,
    onBindView: View.() -> Unit = {}
) {
    insertCustomView(Footer(layoutRes, onBindView))
}

fun CustomViewAdapter<*, out RecyclerView.ViewHolder>.insertHeader(
    @LayoutRes layoutRes: Int,
    onBindView: View.() -> Unit = {}
) {
    insertCustomView(Header(layoutRes, onBindView))
}