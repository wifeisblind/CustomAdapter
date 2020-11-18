package com.example.democustomadapter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.democustomadapter.customviewadapter.EasyAdapter
import com.example.democustomadapter.customviewadapter.EasyAdapter.CustomItemView
import com.example.democustomadapter.customviewadapter.insertFooter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_footer.view.*
import kotlinx.android.synthetic.main.layout_horizontal_item.view.*
import kotlinx.android.synthetic.main.layout_horizontal_scroll.view.*

class MainActivity : AppCompatActivity(), DemoAdapter.DemoHolderClickListener {

    private val demoAdapter: DemoAdapter = DemoAdapter(this)

    private val horizontalAdapter: HorizontalAdapter = HorizontalAdapter()

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv.apply(demoAdapter.setting)
        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollHorizontally(1) ) {
                    viewModel.loadMore()
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        demoAdapter.insertCustomView(HorizontalRecyclerViewHolder(horizontalAdapter))

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

        viewModel.getHorizontalList().observe(this) {
            horizontalAdapter.submitList(it)
        }
    }

    override fun onAddFavorite(position: Int) {
        viewModel.addFavorite(position)
    }

    override fun onDelete(position: Int) {
        viewModel.delete(position)
    }

    class HorizontalRecyclerViewHolder(
            private val adapter: HorizontalAdapter
    ) : CustomItemView(R.layout.layout_horizontal_scroll) {

        override fun getInsertPosition(itemCount: Int): Int = 3

        override fun onViewCreated(view: View) {
            with(view) {
                rvHorizontal.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                rvHorizontal.adapter = adapter
            }
        }
    }

    class HorizontalAdapter : ListAdapter<HorizontalData, HorizontalViewHolder>(HorizontalItemCallback()) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalViewHolder {
            return HorizontalViewHolder(parent)
        }

        override fun onBindViewHolder(holder: HorizontalViewHolder, position: Int) {
            holder.bind(getItem(position))
        }
    }

    class HorizontalViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_horizontal_item, parent, false)
    ) {
        fun bind(data: HorizontalData) = with(itemView) {
            Glide.with(context)
                    .load(data.imgUrl)
                    .into(imgCommodity)
            tvTitle.text = data.title
        }
    }

    class HorizontalItemCallback : DiffUtil.ItemCallback<HorizontalData>() {
        override fun areItemsTheSame(oldItem: HorizontalData, newItem: HorizontalData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HorizontalData, newItem: HorizontalData): Boolean {
            return oldItem == newItem
        }
    }
}