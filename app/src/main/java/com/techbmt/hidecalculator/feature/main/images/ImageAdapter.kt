package com.techbmt.hidecalculator.feature.main.images

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.techbmt.hidecalculator.databinding.ItemImageBinding
import jp.wasabeef.blurry.Blurry


class ImageAdapter : ListAdapter<String, ImageAdapter.ImageViewHolder>(DiffCallBackPhoto()) {
    var listener : OnClickImage?= null
    private var selected = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bindData(getItem(position), listener)
    }

    inner class ImageViewHolder(private val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(imagePath: String, listener: OnClickImage?) {
            binding.run {
                Glide.with(itemView.context).load(imagePath).into(image)
                itemView.setOnClickListener {
                    selected = !selected
                    if(selected) {
                        Blurry.with(itemView.context).capture(itemView).into(image)
                        selection.visibility = View.VISIBLE
                        listener?.clickImage(imagePath)
                    } else {
                        selection.visibility = View.GONE
                        Glide.with(itemView.context).load(imagePath).into(image)
                        listener?.onSecondClick(imagePath)
                    }
                }
            }
        }
    }
}

class DiffCallBackPhoto: DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

}

interface OnClickImage {
    fun clickImage (path: String)
    fun onSecondClick(path: String)
}