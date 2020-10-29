package com.example.democustomadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.recyclerview.widget.RecyclerView
import com.example.democustomadapter.TestAdapter.TestViewHolder
import com.example.democustomadapter.customviewadapter.CustomViewAdapter

class TestAdapter : CustomViewAdapter<TestData, TestViewHolder>(TestDiff()) {

    override fun onCreateNormalViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        return TestViewHolder(parent)
    }

    override fun onBindNormalViewHolder(holder: TestViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener { Toast.makeText(holder.context, "position: $position", LENGTH_SHORT).show() }
    }

    class TestViewHolder(
        parent: ViewGroup
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
    ) {
        val context: Context = itemView.context

        val textView: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(data: TestData) {
            textView.text = data.data
        }
    }

    class TestDiff : CustomViewAdapter.NormalItemCallback<TestData>() {
        override fun areNormalItemsTheSame(oldItem: TestData, newItem: TestData): Boolean {
            return oldItem == newItem
        }

        override fun areNormalContentsTheSame(oldItem: TestData, newItem: TestData): Boolean {
            return oldItem == newItem
        }
    }
}