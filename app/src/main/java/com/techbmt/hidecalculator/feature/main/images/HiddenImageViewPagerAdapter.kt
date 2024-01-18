package com.techbmt.hidecalculator.feature.main.images

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.techbmt.hidecalculator.databinding.ItemVpgHiddenFileBinding

class HiddenImageViewPagerAdapter(
    private val listener: (HiddenFile, Int) -> Unit
) : ListAdapter<HiddenFile, HiddenImageViewPagerAdapter.HiddenImageVPGViewHolder>(DiffCallBackHiddenImageVPG()) {

    inner class HiddenImageVPGViewHolder(private val binding: ItemVpgHiddenFileBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(file: HiddenFile) {
            binding.run {
                Glide.with(itemView.context).load(file.newPath).into(ivHiddenImage)
            }
            itemView.setOnClickListener {
                listener.invoke(file, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HiddenImageVPGViewHolder {
        return HiddenImageVPGViewHolder(ItemVpgHiddenFileBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: HiddenImageVPGViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class DiffCallBackHiddenImageVPG: DiffUtil.ItemCallback<HiddenFile>() {
    override fun areItemsTheSame(oldItem: HiddenFile, newItem: HiddenFile): Boolean {
        return oldItem == newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: HiddenFile, newItem: HiddenFile): Boolean {
        return oldItem == newItem
    }

}