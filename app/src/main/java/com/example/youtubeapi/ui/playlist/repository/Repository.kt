package com.example.youtubeapi.ui.playlist.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.youtubeapi.BuildConfig.API_KEY
import com.example.youtubeapi.utils.Constants
import com.example.youtubeapi.core.network.Resource
import com.example.youtubeapi.model.PlayList
import com.example.youtubeapi.network.RetrofitClient
import com.example.youtubeapi.network.YoutubeApi
import kotlinx.coroutines.Dispatchers

class Repository {

    private var youtubeApi: YoutubeApi = RetrofitClient.create()

    fun fetchYoutubeApiPlayList(): LiveData<Resource<PlayList>> = liveData(Dispatchers.IO) {

        val response = youtubeApi.fetchAllPlayLists(
            Constants.PART,
            Constants.CHANNEL_ID,
            API_KEY,
            Constants.MAX_RESULT
        )
        emit(Resource.loading(response.body()))
        emit(
            if (response.isSuccessful) Resource.success(response.body(), response.code())
            else Resource.error(response.message(), response.body(), response.code())
        )
    }

}