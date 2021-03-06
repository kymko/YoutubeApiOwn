package com.example.youtubeapi.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.youtubeapi.App
import com.example.youtubeapi.core.network.Resource
import com.example.youtubeapi.model.PlayListJs
import com.example.youtubeapi.ui.repository.Repository

class PlayListViewModel(private val repository: Repository) : ViewModel() {

    fun fetchAllPlayLists(): LiveData<Resource<PlayListJs>> {
        return repository.fetchYoutubeApiPlayList()
    }


}