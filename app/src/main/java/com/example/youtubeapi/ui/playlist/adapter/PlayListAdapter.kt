package com.example.youtubeapi.ui.playlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.youtubeapi.databinding.ListItemBinding
import com.example.youtubeapi.extensions.load
import com.example.youtubeapi.model.PlayListItems

class PlayListAdapter : RecyclerView.Adapter<PlayListAdapter.ViewHolder>() {

    private lateinit var onItemClickListener: OnItemClickListener

    private lateinit var binding: ListItemBinding
    private val playList: ArrayList<PlayListItems> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, onItemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(playList[position])
    }

    override fun getItemCount(): Int {
        return playList.size
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun addPlayList(list: ArrayList<PlayListItems>?) {
        if (list != null) {
            playList.addAll(list)
        }
        notifyDataSetChanged()

    }

    inner class ViewHolder(itemView: View, var onItemClickListener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {

        var binding: ListItemBinding = ListItemBinding.bind(itemView)

        fun onBind(playList: PlayListItems?) {

            itemView.setOnClickListener {
                onItemClickListener.onItemClick(playList)
            }

            binding.tvHeader.text = playList?.snippet?.title
            binding.imageView.load(playList?.snippet?.thumbnails?.default?.url.toString())
            (playList?.contentDetails?.itemCount.toString() + " Video series").also {
                binding.tvSeries.text = it
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(playList: PlayListItems?)
    }
}