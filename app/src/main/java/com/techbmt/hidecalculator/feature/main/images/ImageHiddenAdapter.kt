package com.techbmt.hidecalculator.feature.main.images

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.techbmt.hidecalculator.databinding.ItemImageBinding


class ImageHiddenAdapter : ListAdapter<HiddenFile, ImageHiddenAdapter.ImageHiddenViewHolder>(DiffCallBackHiddenPhoto()) {
    var listener : OnClickHiddenImage?= null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHiddenViewHolder {
        return ImageHiddenViewHolder(
            ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImageHiddenViewHolder, position: Int) {
        holder.bindData(getItem(position), listener)
    }

    inner class ImageHiddenViewHolder(private val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(file: HiddenFile, listener: OnClickHiddenImage?) {
            binding.run {
                Glide.with(itemView.context).load(file.newPath).into(image)
                itemView.setOnClickListener {
                    listener?.clickImage(file, adapterPosition)
                }
            }
        }
    }
}

class DiffCallBackHiddenPhoto: DiffUtil.ItemCallback<HiddenFile>() {
    override fun areItemsTheSame(oldItem: HiddenFile, newItem: HiddenFile): Boolean {
        return oldItem == newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: HiddenFile, newItem: HiddenFile): Boolean {
        return oldItem == newItem
    }

}

interface OnClickHiddenImage {
    fun clickImage(file: HiddenFile, pos: Int)
}