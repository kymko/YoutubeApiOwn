package com.example.youtubeapi.ui.playlist

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.youtubeapi.core.network.Status
import com.example.youtubeapi.core.ui.BaseActivity
import com.example.youtubeapi.databinding.ActivityPlayListBinding
import com.example.youtubeapi.extensions.showToast
import com.example.youtubeapi.model.PlayListJs
import com.example.youtubeapi.ui.detail.DetailActivity
import com.example.youtubeapi.ui.playlist.adapter.PlayListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListActivity : BaseActivity<ActivityPlayListBinding>() {

    private val viewModel: PlayListViewModel by viewModel()

    private val playListAdapter: PlayListAdapter by lazy { PlayListAdapter(this::clickListener) }

    override fun setupUI() {
    }

    override fun setupLiveData() {

        viewModel.fetchAllPlayLists().observe(this) {

            when (it.status) {
                Status.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    playListAdapter.addPlayList(it.data?.items as ArrayList<PlayListJs.Item>?)
                }
                Status.ERROR -> {
                    binding.progress.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                    binding.progress.visibility = View.VISIBLE
                }
            }
        }

        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(this@PlayListActivity)
            adapter = playListAdapter
        }
    }

    private fun clickListener(playListJs: PlayListJs.Item) {
        Intent(this@PlayListActivity, DetailActivity::class.java).apply {
            putExtra(TITLE, playListJs.snippet.title)
            putExtra(PLAY_LIST_ID, playListJs.id)
            startActivity(this)
        }
    }

    override fun showDisconnectState() {

        if (!internetAvailable) {
            binding.layoutDisconnect.visibility = View.VISIBLE
            binding.recyclerview.visibility = View.GONE
            showToast("internet not available")
        } else {
            binding.layoutDisconnect.visibility = View.GONE
            binding.recyclerview.visibility = View.VISIBLE
        }
    }


    override fun inflateBinding(inflater: LayoutInflater): ActivityPlayListBinding {
        return ActivityPlayListBinding.inflate(layoutInflater)
    }

    companion object {
        const val TITLE = "title"
        const val PLAY_LIST_ID = "id"
    }

}