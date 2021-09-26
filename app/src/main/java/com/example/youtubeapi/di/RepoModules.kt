package com.example.youtubeapi.di

import com.example.youtubeapi.ui.repository.Repository
import org.koin.core.module.Module
import org.koin.dsl.module

val repoModules:Module = module {
    single { Repository() }
}