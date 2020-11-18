package com.example.democustomadapter.easyadapter

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView


class Footer(
    @LayoutRes private val layoutRes: Int,
    private val onBindView: View.() -> Unit
) : EasyAdapter.CustomItemView(layoutRes) {

    override fun onViewCreated(view: View) {
        onBindView(view)
    }

    override fun getInsertPosition(itemCount: Int): Int = itemCount - 1
}

class Header(
    @LayoutRes private val layoutRes: Int,
    private val onBindView: View.() -> Unit
) : EasyAdapter.CustomItemView(layoutRes) {

    override fun onViewCreated(view: View) {
        onBindView(view)
    }

    override fun getInsertPosition(itemCount: Int): Int = 0
}


fun EasyAdapterDelegate<*, *>.insertFooter(
    @LayoutRes layoutRes: Int,
    onBindView: View.() -> Unit = {}
) {
    insertCustomView(Footer(layoutRes, onBindView))
}

fun EasyAdapterDelegate<*, *>.insertHeader(
    @LayoutRes layoutRes: Int,
    onBindView: View.() -> Unit = {}
) {
    insertCustomView(Header(layoutRes, onBindView))
}