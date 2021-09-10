package com.example.youtubeapi.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.youtubeapi.core.network.Status
import com.example.youtubeapi.core.ui.BaseActivity
import com.example.youtubeapi.databinding.ActivityDetailBinding
import com.example.youtubeapi.ui.detail.adapter.DetailAdapter

class DetailActivity : BaseActivity<ActivityDetailBinding>() {

    private var viewModel: DetailViewModel? = null
    private val detailAdapter: DetailAdapter by lazy { DetailAdapter() }

    override fun setupUI() {
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
    }



    override fun setupLiveData() {

        viewModel?.fetchAllPlayLists()?.observe(this, { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    detailAdapter.addPlayList(response.data?.items)
                }


                Status.ERROR -> {
                    binding.progress.visibility = View.GONE
                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                }


                Status.LOADING -> {
                    binding.progress.visibility = View.VISIBLE
                }
            }
        })
        binding.included.recyclerviewPlay.apply {
            layoutManager = LinearLayoutManager(this@DetailActivity)
            adapter = detailAdapter
        }
        val string = intent.getStringExtra("key")
        binding.tvHeaderPlay.text = string

    }

    override fun showDisconnectState() {

//        val networkConnection = NetworkConnection(this)
//
//        networkConnection.observe(this) { isConnected ->
//            if (isConnected) {
//                binding.layoutDisconnect.visibility = View.GONE
//                binding.coordinatorLayout.visibility = View.VISIBLE
//            } else {
//                binding.layoutDisconnect.visibility = View.VISIBLE
//                binding.coordinatorLayout.visibility = View.GONE
//            }
//        }
    }

    override fun inflateBinding(inflater: LayoutInflater): ActivityDetailBinding {
        return ActivityDetailBinding.inflate(layoutInflater)
    }
}
