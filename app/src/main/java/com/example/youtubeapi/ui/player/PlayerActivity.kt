package com.example.youtubeapi.ui.player

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.youtubeapi.core.ui.BaseActivity
import com.example.youtubeapi.databinding.ActivityPlayerBinding
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import android.util.SparseArray
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.example.youtubeapi.ui.detail.DetailActivity.Companion.DESCRIPTION
import com.example.youtubeapi.ui.detail.DetailActivity.Companion.TITLE
import com.example.youtubeapi.ui.detail.DetailActivity.Companion.VIDEO_ID
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource

class PlayerActivity : BaseActivity<ActivityPlayerBinding>() {

    private var viewModel: PlayerViewModel? = null

    private var mPlayer: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private var videoId:String? = null

    override fun setupUI() {

        viewModel = ViewModelProvider(this).get(PlayerViewModel::class.java)
        binding.backContainer.setOnClickListener {
            finish()
        }
        initPlayer()
        val title = intent.getStringExtra(TITLE)
        val desc = intent.getStringExtra(DESCRIPTION)
        binding.tvTitle.text = title
        binding.tvDesc.text = desc
    }

    override fun setupLiveData() {

    }

    override fun showDisconnectState() {

    }

    override fun inflateBinding(inflater: LayoutInflater): ActivityPlayerBinding {
        return ActivityPlayerBinding.inflate(layoutInflater)
    }

    private fun initPlayer() {

        mPlayer = SimpleExoPlayer.Builder(this).build()
        binding.exoPlayer.player = mPlayer
        videoId = intent.getStringExtra(VIDEO_ID)
        val youtubeLink = "http://youtube.com/watch?v=$videoId"

        object : YouTubeExtractor(this) {
            override fun onExtractionComplete(ytFiles: SparseArray<YtFile>?, vMeta: VideoMeta?) {

                if (ytFiles != null) {
                    val itag = 137
                    val audioTag = 140
                    val videoUrl = ytFiles[itag].url
                    val audioUrl = ytFiles[audioTag].url
                    val audioSourse: MediaSource =
                        ProgressiveMediaSource.Factory(DefaultHttpDataSource.Factory())
                            .createMediaSource(
                                MediaItem.fromUri(audioUrl)
                            )
                    val videoSource: MediaSource =
                        ProgressiveMediaSource.Factory(DefaultHttpDataSource.Factory())
                            .createMediaSource(
                                MediaItem.fromUri(videoUrl)
                            )
                    mPlayer?.setMediaSource(
                        MergingMediaSource(true, videoSource, audioSourse),
                        true
                    )
                    mPlayer?.prepare()
                    mPlayer?.playWhenReady = playWhenReady
                    mPlayer?.seekTo(currentWindow, playbackPosition)
                }
            }
        }.extract(youtubeLink)
    }

    override fun onStart() {
        super.onStart()
        if (com.google.android.exoplayer2.util.Util.SDK_INT >= 24) {
            initPlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if (com.google.android.exoplayer2.util.Util.SDK_INT < 24 || mPlayer == null) {
            initPlayer()
            hideSystemUi()
        }
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        binding.exoPlayer.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LOW_PROFILE
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    override fun onPause() {
        super.onPause()
        if (com.google.android.exoplayer2.util.Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        if (com.google.android.exoplayer2.util.Util.SDK_INT < 24) {
            releasePlayer()
            super.onStop()
        }
    }

    private fun releasePlayer() {
        if (mPlayer == null) {
            playWhenReady = mPlayer!!.playWhenReady
            playbackPosition = mPlayer!!.currentPosition
            currentWindow = mPlayer!!.currentWindowIndex
            mPlayer!!.release()
            mPlayer = null
        }
    }
}