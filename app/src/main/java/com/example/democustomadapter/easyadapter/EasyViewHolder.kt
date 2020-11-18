package com.example.democustomadapter.easyadapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

abstract class EasyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), LayoutContainer {

    override val containerView: View = itemView

    lateinit var helper: EasyAdapterHelper<*>

    protected val normalPosition: Int get() = helper.getNormalPos(adapterPosition)
}