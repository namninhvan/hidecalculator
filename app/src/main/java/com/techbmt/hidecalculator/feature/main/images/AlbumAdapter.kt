package com.techbmt.hidecalculator.feature.main.images

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.techbmt.hidecalculator.databinding.ItemFolderImagesBinding

class AlbumAdapter :
    ListAdapter<AlbumModel, AlbumAdapter.AlbumViewHolder>(DiffCallBackAlbumsImage()) {
    var listener: IClickOnFolder? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder(
            ItemFolderImagesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    inner class AlbumViewHolder(private val binding: ItemFolderImagesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(album: AlbumModel) {
            binding.apply {
                tvAlbumName.text = album.parentFolder
                tvAlbumSize.text = album.path.size.toString()
                Glide.with(itemView.context).load(album.path[0]).into(ivAlbum)
                itemView.setOnClickListener {
                    listener?.onClickFolder(album, adapterPosition)
                }
            }
        }
    }


}

class DiffCallBackAlbumsImage : DiffUtil.ItemCallback<AlbumModel>() {
    override fun areItemsTheSame(oldItem: AlbumModel, newItem: AlbumModel): Boolean {
        return oldItem == newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: AlbumModel, newItem: AlbumModel): Boolean {
        return oldItem == newItem
    }

}

interface IClickOnFolder {
    fun onClickFolder(album: AlbumModel,position: Int)
}