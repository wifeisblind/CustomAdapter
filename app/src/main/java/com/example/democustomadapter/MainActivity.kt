package com.example.democustomadapter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.democustomadapter.customviewadapter.insertFooter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_footer.view.*

class MainActivity : AppCompatActivity() {

    private val testAdapter: TestAdapter by lazy { TestAdapter() }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = testAdapter
        }

//        testAdapter.insertHeader(R.layout.layout_cutom_item) {
//            btnTest.text = "Header"
//            btnTest.setOnClickListener {
//                Toast.makeText(this@MainActivity, "This is Header", LENGTH_SHORT).show()
//            }
//        }

        viewModel.getHasMore().observe(this) { hasMore ->
            testAdapter.insertFooter(R.layout.layout_footer) {
                if (hasMore) {
                    layMore.visibility = VISIBLE
                    layEnd.visibility = INVISIBLE
                } else {
                    layMore.visibility = INVISIBLE
                    layEnd.visibility = VISIBLE
                }
            }
        }

        viewModel.getTestList().observe(this) {
            testAdapter.submitNormalList(it)
        }
    }
}