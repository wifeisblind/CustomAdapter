package com.example.democustomadapter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import com.example.democustomadapter.customviewadapter.insertFooter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_footer.view.*

class MainActivity : AppCompatActivity(), DemoAdapter.OnAddFavoriteClickListener {

    private val demoAdapter: DemoAdapter = DemoAdapter(this)

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv.apply(demoAdapter.setting)

        viewModel.getHasMore().observe(this) { hasMore ->
            demoAdapter.insertFooter(R.layout.layout_footer) {
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
            demoAdapter.submitList(it)
        }
    }

    override fun onAddFavorite(position: Int) {
        viewModel.addFavorite(position)
    }
}