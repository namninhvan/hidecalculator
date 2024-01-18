package com.techbmt.hidecalculator.feature.main.video

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.techbmt.hidecalculator.core.convertDuration
import com.techbmt.hidecalculator.databinding.ItemVideoBinding
import com.techbmt.hidecalculator.feature.main.images.OnClickImage
import java.io.File

class VideoAdapter(
    private val listener: OnClickVideo,
    private var type: Int
) : ListAdapter<VideoModel, VideoAdapter.VideoViewHolder>(VideoDiffCallback()) {

    private var selected = false

    inner class VideoViewHolder(private val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(video: VideoModel) {
            binding.apply {
                val time = convertDuration(video.duration)
                val file = File(video.originPath)
                Glide.with(itemView.context).load(video.thumb).into(videoThumbnail)
                tvName.text = file.name
                tvDuration.text = time
                if(type == 1) {
                    itemView.setOnClickListener {
                        selected = !selected
                        if (selected) {
                            binding.checker.visibility = View.VISIBLE
                            listener.clickVideo(video)
                        } else {
                            binding.checker.visibility = View.GONE
                            listener.onSecondClick(video)
                        }
                    }
                } else if(type == 2) {
                    itemView.setOnClickListener {
                        listener.clickVideo(video)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(
            ItemVideoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}


class VideoDiffCallback : DiffUtil.ItemCallback<VideoModel>() {
    override fun areItemsTheSame(oldItem: VideoModel, newItem: VideoModel): Boolean {
        return oldItem.originPath == newItem.originPath
    }

    override fun areContentsTheSame(oldItem: VideoModel, newItem: VideoModel): Boolean {
        return oldItem == newItem
    }
}

interface OnClickVideo {
    fun clickVideo(video: VideoModel)
    fun onSecondClick(video: VideoModel)
}