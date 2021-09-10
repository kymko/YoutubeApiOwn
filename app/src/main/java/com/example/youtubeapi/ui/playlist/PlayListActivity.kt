package com.example.youtubeapi.ui.playlist

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.youtubeapi.core.network.Status
import com.example.youtubeapi.core.ui.BaseActivity
import com.example.youtubeapi.databinding.ActivityPlayListBinding
import com.example.youtubeapi.model.PlayListItems
import com.example.youtubeapi.ui.detail.DetailActivity
import com.example.youtubeapi.ui.playlist.adapter.PlayListAdapter

class PlayListActivity : BaseActivity<ActivityPlayListBinding>() {

    private var viewModel: PlayListViewModel? = null
    private val playListAdapter : PlayListAdapter by lazy { PlayListAdapter() }

    override fun setupUI() {
        viewModel = ViewModelProvider(this).get(PlayListViewModel::class.java)
    }

    override fun setupLiveData() {

        viewModel?.fetchAllPlayLists()?.observe(this) {

            when(it.status){
                Status.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    playListAdapter.addPlayList(it.data?.items)
                }
                Status.ERROR -> {
                    binding.progress.visibility = View.GONE
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
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
        playListAdapter.setOnItemClickListener(object : PlayListAdapter.OnItemClickListener{
            override fun onItemClick(playList: PlayListItems?) {
                val intent = Intent(this@PlayListActivity,DetailActivity::class.java)
                intent.putExtra("key",playList?.snippet?.title)
                startActivity(intent)
            }

        })
    }


    override fun showDisconnectState() {

//        val networkConnection = NetworkConnection(this)
//
//        networkConnection.observe(this) { isConnected ->
//            if (isConnected) {
//                binding.layoutDisconnect.visibility = View.GONE
//                binding.recyclerview.visibility = View.VISIBLE
//            } else {
//                binding.layoutDisconnect.visibility = View.VISIBLE
//                binding.recyclerview.visibility = View.GONE
//            }
//        }

    }

    override fun inflateBinding(inflater: LayoutInflater): ActivityPlayListBinding {
        return ActivityPlayListBinding.inflate(layoutInflater)
    }
}


//1. Создать свой ApiKey и ознакомиться с документацией
//2. Добавить в класс playlist поле "items", отрисовать первых 2 экрана из фигмы (Проверка на интернет, и список всех PlayList)
//3. сделать переход на новую активити и передаете туда id и её отображаете тостом
//
//Также прочитайте про корутины желательно
//
//Доп: в PlayListActivity попробуйте реализовать пагинацию с помощью ViewType с RecyclerView