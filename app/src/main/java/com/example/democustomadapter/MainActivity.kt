package com.example.democustomadapter

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.democustomadapter.customviewadapter.CustomViewAdapter
import com.example.democustomadapter.customviewadapter.CustomViewAdapter.CustomItemView
import com.example.democustomadapter.customviewadapter.insertFooter
import com.example.democustomadapter.customviewadapter.insertHeader
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_cutom_item.view.*
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity() {

    private val testAdapter: TestAdapter by lazy { TestAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv.layoutManager = LinearLayoutManager(this)

        testAdapter.insertFooter(R.layout.layout_cutom_item) {
            btnTest.text = "Footer"
            btnTest.setOnClickListener {
                Toast.makeText(this@MainActivity, "This is Footer", LENGTH_SHORT).show()
            }
        }

        val middle = MiddleViewHolder {
            btnTest.text = "Middle"
            btnTest.setOnClickListener {
                Toast.makeText(this@MainActivity, "This is Middle", LENGTH_SHORT).show()
            }
        }

        testAdapter.insertHeader(R.layout.layout_cutom_item) {
            btnTest.text = "Header"
            btnTest.setOnClickListener {
                Toast.makeText(this@MainActivity, "This is Header", LENGTH_SHORT).show()
            }
        }

        rv.adapter = testAdapter.apply {
            insertCustomView(middle)
        }

        testAdapter.submitNormalList(List(15) { index -> TestData("This is Test: $index") })

        lifecycleScope.launchWhenResumed {
            delay(3000)
            testAdapter.submitNormalList(List(9) { index -> TestData("This is Test: $index") })
            delay(3000)
            testAdapter.submitNormalList(List(15) { index -> TestData("This is Test: $index") })
        }
    }

    class MiddleViewHolder(
        private val bind: View.() -> Unit
    ) : CustomItemView(R.layout.layout_cutom_item) {

        override fun onViewCreated(view: View) {
            bind(view)
        }

        override fun getInsertPosition(itemCount: Int): Int = 11
    }

    class TestAdapter : CustomViewAdapter<TestData, TestViewHolder>(TestDiff()) {
        override fun onCreateNormalViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
            return TestViewHolder(parent)
        }

        override fun onBindNormalViewHolder(holder: TestViewHolder, position: Int) {
            holder.bind(getItem(position))
            holder.itemView.setOnClickListener { Toast.makeText(holder.context, "position: $position", LENGTH_SHORT).show() }
        }
    }

    data class TestData(val data: String)

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