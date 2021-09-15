package com.example.youtubeapi.ui.player

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.youtubeapi.R
import com.example.youtubeapi.core.ui.BaseActivity
import com.example.youtubeapi.databinding.ActivityPlayerBinding
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util.getUserAgent
import android.util.SparseArray
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile

class PlayerActivity : BaseActivity<ActivityPlayerBinding>() {

    private var viewModel:PlayerViewModel? = null
    private var mPlayer: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private var urlVideo = ""

    private val url =
        "https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8"

    private val youtubeLink = "http://youtube.com/watch?v=Bag1gUxuU0g"

    override fun setupUI() {
        viewModel = ViewModelProvider(this).get(PlayerViewModel::class.java)

        binding.backContainer.setOnClickListener{
            finish()
        }
        down()
    }

    override fun setupLiveData() {

    }

    override fun showDisconnectState() {

    }

    override fun inflateBinding(inflater: LayoutInflater): ActivityPlayerBinding {
        return ActivityPlayerBinding.inflate(layoutInflater)
    }

    private fun down() {

        object : YouTubeExtractor(this) {
            override fun onExtractionComplete(ytFiles: SparseArray<YtFile>?, vMeta: VideoMeta?) {
                if (ytFiles != null) {
                    val itag = 22
                    val downloadUrl: String = ytFiles[itag].getUrl()
                    urlVideo = downloadUrl
                }
            }
        }.extract(youtubeLink)

    }

    private fun initPlayer(){
        mPlayer = SimpleExoPlayer.Builder(this).build()
        binding.exoPlayer.player = mPlayer
        mPlayer!!.playWhenReady = true
        mPlayer!!.seekTo(currentWindow,playbackPosition)
        mPlayer!!.prepare(buildMediaSource(), false, false)
    }

    override fun onStart() {
        super.onStart()
        if (com.google.android.exoplayer2.util.Util.SDK_INT >= 24){
            initPlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (com.google.android.exoplayer2.util.Util.SDK_INT < 24 || mPlayer == null){
            initPlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (com.google.android.exoplayer2.util.Util.SDK_INT < 24){
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (com.google.android.exoplayer2.util.Util.SDK_INT < 24){
            releasePlayer()
        }
        mPlayer?.stop()
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        binding.exoPlayer.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

    }

    private fun releasePlayer() {
        if (mPlayer == null) {
            return
        }
        playWhenReady = mPlayer!!.playWhenReady
        playbackPosition = mPlayer!!.currentPosition
        currentWindow = mPlayer!!.currentWindowIndex
        mPlayer!!.release()
        mPlayer = null
    }

    private fun buildMediaSource(): MediaSource {
        val userAgent = getUserAgent(
            binding.exoPlayer.context,
            binding.exoPlayer.context.getString(R.string.app_name)
        )

        val dataSourceFactory = DefaultHttpDataSourceFactory(userAgent)
        return HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(urlVideo))
    }
}