package com.example.youtubeapi.ui.player

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import com.example.youtubeapi.core.ui.BaseActivity
import com.example.youtubeapi.databinding.ActivityPlayerBinding
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import android.util.SparseArray
import android.view.View
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.example.youtubeapi.R
import com.example.youtubeapi.databinding.DownloadDialogBinding
import com.example.youtubeapi.extensions.showToast
import com.example.youtubeapi.ui.detail.DetailActivity.Companion.DESCRIPTION
import com.example.youtubeapi.ui.detail.DetailActivity.Companion.TITLE
import com.example.youtubeapi.ui.detail.DetailActivity.Companion.VIDEO_ID
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("StaticFieldLeak")
class PlayerActivity : BaseActivity<ActivityPlayerBinding>() {

    private val viewModel: PlayerViewModel by viewModel()
    private lateinit var dialogBinding: DownloadDialogBinding
    private var mPlayer: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private var videoId: String? = null

    override fun setupUI() {

        binding.backContainer.setOnClickListener { finish() }

        initPlayer()

        val title = intent.getStringExtra(TITLE)
        val desc = intent.getStringExtra(DESCRIPTION)
        binding.tvTitle.text = title
        binding.tvDesc.text = desc

        binding.cardView.setOnClickListener {

            dialogBinding = DownloadDialogBinding.inflate(LayoutInflater.from(this))
            val view: View = dialogBinding.root
            val mBuilder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
                .setView(view)
                .setTitle("Select video quality")
            val mAlertDialog = mBuilder.show()

            dialogBinding.btnDownload.setOnClickListener {

                when (val checkRadioButton = dialogBinding.radioGroup.checkedRadioButtonId) {
                    -1 -> {
                        showToast("Nothing selected!")
                    }
                    else -> {
                        showToast(checkRadioButton.toString())
                    }

                }

                mAlertDialog.dismiss()
            }
        }
    }

    override fun setupLiveData() {

    }

    override fun showDisconnectState() {

    }

    private fun initPlayer() {

        mPlayer = SimpleExoPlayer.Builder(this).build()
        binding.exoPlayer.player = mPlayer
        videoId = intent.getStringExtra(VIDEO_ID)
        val youtubeLink = "http://youtube.com/watch?v=$videoId"

        object : YouTubeExtractor(this) {

            override fun onExtractionComplete(
                ytFiles: SparseArray<YtFile>?,
                vMeta: VideoMeta?,
            ) {

                if (ytFiles != null) {
                    val itag = 137
                    val audioTag = 140
                    val videoUrl = ytFiles[itag].url
                    val audioUrl = ytFiles[audioTag].url
                    val audioSource: MediaSource =
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
                        MergingMediaSource(true, videoSource, audioSource),
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
        initPlayer()
    }

    override fun onResume() {
        super.onResume()
        initPlayer()
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    override fun onStop() {
        releasePlayer()
        super.onStop()
        mPlayer?.stop()

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

    override fun inflateBinding(inflater: LayoutInflater): ActivityPlayerBinding {
        return ActivityPlayerBinding.inflate(layoutInflater)
    }
}