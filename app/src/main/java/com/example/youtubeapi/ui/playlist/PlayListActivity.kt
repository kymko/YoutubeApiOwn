package com.example.youtubeapi.ui.playlist

import android.content.Intent
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.youtubeapi.R
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

    private lateinit var playListAdapter:PlayListAdapter

    private var mainMenu: Menu? = null

    override fun setupUI() {
        playListAdapter = PlayListAdapter( this::showDeleteMenu,this::click)
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(this@PlayListActivity)
            adapter = playListAdapter
        }
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

    private fun click(item:List<PlayListJs.Item>,position:Int){
        Intent(this@PlayListActivity, DetailActivity::class.java).apply {
            putExtra(TITLE, item[position].snippet.title)
            putExtra(PLAY_LIST_ID, item[position].id)
            startActivity(this)
        }
    }

    override fun inflateBinding(inflater: LayoutInflater): ActivityPlayListBinding {
        return ActivityPlayListBinding.inflate(layoutInflater)
    }

    companion object {
        const val TITLE = "title"
        const val PLAY_LIST_ID = "id"
    }

    private fun showDeleteMenu(show: Boolean) {
        mainMenu?.findItem(R.id.mDelete)?.isVisible = show
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        mainMenu = menu
        menuInflater.inflate(R.menu.custom_menu, mainMenu)
        showDeleteMenu(false)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mDelete -> {
//                delete()
            }
        }
        return super.onOptionsItemSelected(item)

    }

}