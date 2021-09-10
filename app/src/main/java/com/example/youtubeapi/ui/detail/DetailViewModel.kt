package com.example.youtubeapi.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.youtubeapi.App
import com.example.youtubeapi.core.network.Resource
import com.example.youtubeapi.model.PlayList

class DetailViewModel : ViewModel() {

    fun fetchAllPlayLists(): LiveData<Resource<PlayList>> {
        return App.repository.fetchYoutubeApiPlayList()
    }

}