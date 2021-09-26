# YoutubePlayer
This app will allow play video from Youtube

## Screenshots
<img width = "211" alt="1" src = "https://github.com/kymko/YoutubeApiOwn/blob/master/app/src/main/java/com/example/youtubeapi/screens/img_4.png"><img width = "211" alt="1" src = "https://github.com/kymko/YoutubeApiOwn/blob/master/app/src/main/java/com/example/youtubeapi/screens/img_1.png"><img width = "211" alt="1" src = "https://github.com/kymko/YoutubeApiOwn/blob/master/app/src/main/java/com/example/youtubeapi/screens/img_2.png"><img width = "211" alt="1" src = "https://github.com/kymko/YoutubeApiOwn/blob/master/app/src/main/java/com/example/youtubeapi/screens/img_3.png">

## Getting started

Install my-project from

```bash

https://github.com/kymko/YoutubeApiOwn
  

```
## Features

- Supports Light/dark mode
- Subscription to favorite playlist 
- Video Playback
- Audio Playback
- Playback controls outside of the app
## API Reference

#### YouTube Data API :

- Base URL 
```http
 https://www.googleapis.com/youtube/v3/
```
- End point
```http
  /playlists
```
#### Get all item

```http
  GET/playlistItems
```
## Used Libraries 

- Retrofit https://square.github.io/retrofit/
- Glide https://github.com/bumptech/glide
- Kotlin Extensions and Coroutines https://developer.android.com/kotlin/coroutines
- Koin DI  https://insert-koin.io
- Youtube Extractor https://github.com/HaarigerHarald/android-youtubeExtractor
- Exoplayer https://developer.android.com/codelabs/exoplayer-intro#2



## Architecture 

- MVVM
- Repository pattern 
- Separation of concerns
- Drive UI from a model
- Base Activity
- Base ViewModel
