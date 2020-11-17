package com.example.democustomadapter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.democustomadapter.ItemData.Footer
import com.example.democustomadapter.ItemData.Normal
import com.example.democustomadapter.customviewadapter.insertFooter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_footer.view.*
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity() {

    private val testAdapter: TestAdapter = TestAdapter()

    private val testAdapter2: TestAdapter2 = TestAdapter2()

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv.apply(testAdapter.setting)

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
            testAdapter.submitList(it)
        }

        lifecycleScope.launchWhenResumed {
            List(10) { index ->
                when(index) {
                    9 -> Footer(true)
                    else -> Normal("This is Test: $index")
                }
            }.let {
                testAdapter2.submitList(it)
            }

            delay(3_000)

            val a = List(15) { index ->
                when(index) {
                    14 -> Footer(true)
                    else -> Normal("This is Test: $index")
                }
            }

            val b = List(15) { index ->
                when(index) {
                    14 -> Footer(false)
                    else -> Normal("This is Test: $index")
                }
            }

            testAdapter2.submitList(a)
            testAdapter2.submitList(b)
        }
    }
}