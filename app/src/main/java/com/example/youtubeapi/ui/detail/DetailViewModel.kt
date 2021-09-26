package com.example.youtubeapi.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.youtubeapi.App
import com.example.youtubeapi.core.network.Resource
import com.example.youtubeapi.model.PlayListItem
import com.example.youtubeapi.ui.repository.Repository

class DetailViewModel(private val repository: Repository) : ViewModel() {

    fun fetchPlayListItems(playListId:String):LiveData<Resource<PlayListItem>>{
        return repository.fetchYoutubeApiPlayListItems(playListId)
    }
}