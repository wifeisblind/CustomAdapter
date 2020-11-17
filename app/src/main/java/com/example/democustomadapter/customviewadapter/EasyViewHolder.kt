package com.example.democustomadapter.customviewadapter

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class EasyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract val layoutRes: Int
}