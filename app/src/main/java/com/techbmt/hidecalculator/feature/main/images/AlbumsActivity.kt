package com.techbmt.hidecalculator.feature.main.images

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.techbmt.hidecalculator.core.BaseActivity
import com.techbmt.hidecalculator.databinding.ActivityAlbumsBinding


class AlbumsActivity : BaseActivity<ActivityAlbumsBinding>(), IClickOnFolder {

    companion object {
        const val INDEX_FOLDER = "index_folder"
        const val ALBUM = "album"
    }

    private var albumAdapter = AlbumAdapter()
    private lateinit var imageViewModel: ImageViewModel

    @SuppressLint("NotifyDataSetChanged")
    override fun initView() {
        albumAdapter.listener = this
        imageViewModel = ViewModelProvider(this)[ImageViewModel::class.java]

        binding.back.setOnClickListener {
            finish()
        }

        if(isPermissionGranted()) {
            imageViewModel.getImagePath(this)
        } else {
            requestPermissions(
                arrayOf(
                    if (isAndroid13()) Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 101
            )
        }

        imageViewModel.listImages.observe(this) { listAlbums ->
            albumAdapter.submitList(listAlbums)
            Log.i("size", listAlbums.size.toString())
            binding.rcvImage.adapter?.notifyDataSetChanged()
        }

        binding.apply {
            rcvImage.adapter = albumAdapter
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101
            && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            imageViewModel.getImagePath(this)
        } else {
            showPermissionDenied()
        }
        return
    }

    override fun getLayout(): ActivityAlbumsBinding {
        return ActivityAlbumsBinding.inflate(layoutInflater)
    }

    override fun onClickFolder(album: AlbumModel, position: Int) {
        val intent = Intent(this@AlbumsActivity, ImagesActivity::class.java)
        val bundle = Bundle()
        bundle.putInt(INDEX_FOLDER, position)
        bundle.putSerializable(ALBUM, album)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

}