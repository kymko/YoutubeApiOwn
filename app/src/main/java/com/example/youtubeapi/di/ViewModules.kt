package com.example.youtubeapi.di

import com.example.youtubeapi.ui.detail.DetailViewModel
import com.example.youtubeapi.ui.player.PlayerViewModel
import com.example.youtubeapi.ui.playlist.PlayListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val viewModules:Module = module {
    viewModel { PlayListViewModel(get()) }
    viewModel { DetailViewModel(get()) }
    viewModel { PlayerViewModel(get()) }
}