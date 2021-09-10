package com.example.youtubeapi.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.youtubeapi.App
import com.example.youtubeapi.core.network.Resource
import com.example.youtubeapi.model.PlayList

class PlayListViewModel : ViewModel() {

    fun fetchAllPlayLists(): LiveData<Resource<PlayList>> {
        return App.repository.fetchYoutubeApiPlayList()
    }


}