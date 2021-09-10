package com.example.youtubeapi.ui.detail.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.youtubeapi.databinding.ListDetailBinding
import com.example.youtubeapi.databinding.ListItemBinding
import com.example.youtubeapi.extensions.load
import com.example.youtubeapi.model.PlayListItems

class DetailAdapter :RecyclerView.Adapter<DetailAdapter.ViewHolder>() {

    private lateinit var binding: ListDetailBinding
    private val playList:ArrayList<PlayListItems> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ListDetailBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(playList[position])
    }

    override fun getItemCount(): Int {
        return playList.size
    }

    fun addPlayList(list: ArrayList<PlayListItems>?){
        if (list != null) {
            playList.addAll(list)
        }
        notifyDataSetChanged()

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var binding: ListDetailBinding = ListDetailBinding.bind(itemView)

        fun onBind(playList: PlayListItems?) {

            Log.d("tag","titleSnip: ${playList?.snippet?.title}")
            binding.tvHeader.text = playList?.snippet?.title
            binding.imageView.load(playList?.snippet?.thumbnails?.default?.url.toString())
            Log.d("tag","series : "+playList?.contentDetails?.itemCount)
            (playList?.contentDetails?.itemCount.toString()+" Video series").also { binding.tvSeries.text = it }
        }
    }
}