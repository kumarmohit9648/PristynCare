package com.mohit.pristyncare.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.mohit.pristyncare.R
import com.mohit.pristyncare.data.repository.NetworkState
import com.mohit.pristyncare.databinding.ActivityMainBinding
import com.mohit.pristyncare.ui.adapter.ImagePagedListAdapter
import com.mohit.pristyncare.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        private var SPAN = 1
    }

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageAdapter = ImagePagedListAdapter(this)
        val gridLayoutManager = GridLayoutManager(this, SPAN)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = imageAdapter.getItemViewType(position)
                return if (viewType == imageAdapter.IMAGE_VIEW_TYPE) 1                              // IMAGE_VIEW_TYPE will occupy 1 out of 2 span
                else SPAN                                                                              // NETWORK_VIEW_TYPE will occupy all 3 span
            }
        }

        binding.rvImageList.layoutManager = gridLayoutManager
        binding.rvImageList.setHasFixedSize(true)
        binding.rvImageList.adapter = imageAdapter

        viewModel.imagePagedList.observe(this, {
            imageAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, {
            progress_bar.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) {
                imageAdapter.setNetworkState(it)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item_view_option, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_list -> {
                SPAN = 1
                changeGrid()
            }
            R.id.action_grid_2 -> {
                SPAN = 2
                changeGrid()
            }
            R.id.action_grid_3 -> {
                SPAN = 3
                changeGrid()
            }
        }
        return true;
    }

    private fun changeGrid() {
        binding.rvImageList.layoutManager = GridLayoutManager(this, SPAN)
        binding.rvImageList.adapter?.notifyDataSetChanged()
    }
}