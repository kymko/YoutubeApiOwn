package com.example.youtubeapi.ui.playlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.youtubeapi.R
import com.example.youtubeapi.databinding.ListItemBinding
import com.example.youtubeapi.extensions.load
import com.example.youtubeapi.model.PlayListJs

class PlayListAdapter(
    private val showMenuDelete: (Boolean)->Unit,
    private val click:(item:List<PlayListJs.Item>,position:Int)->Unit
) :
    RecyclerView.Adapter<PlayListAdapter.ViewHolder>() {

    private lateinit var binding: ListItemBinding
    private val playList: ArrayList<PlayListJs.Item> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(playList[position])
        holder.itemView.setOnClickListener {
            click.invoke(playList,position)
        }
    }

    override fun getItemCount(): Int {
        return playList.size
    }

    fun addPlayList(list: ArrayList<PlayListJs.Item>?) {
        if (list != null) {
            playList.addAll(list)
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var binding: ListItemBinding = ListItemBinding.bind(itemView)

        fun onBind(playList: PlayListJs.Item?) {

            binding.tvHeader.text = playList?.snippet?.title
            binding.imageView.load(playList?.snippet?.thumbnails?.high?.url.toString())
            (playList?.contentDetails?.itemCount.toString() + " Video series").also {
                binding.tvSeries.text = it
            }
        }
    }
}