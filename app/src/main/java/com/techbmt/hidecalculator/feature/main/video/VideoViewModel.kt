package com.techbmt.hidecalculator.feature.main.video

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.techbmt.hidecalculator.feature.main.images.AlbumModel


class VideoViewModel : ViewModel() {

    val listVideos = MutableLiveData<List<VideoModel>>()
    private val mListVideoModel = ArrayList<VideoModel>()
    private var isInsideFolder = false

    fun getVideoPath(applicationContext: Context) {

        mListVideoModel.clear()
        val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media._ID, MediaStore.Video.Thumbnails.DATA, MediaStore.Video.Media.DURATION)
        val orderBy = MediaStore.Images.Media.DATE_TAKEN
        val cursor: Cursor? = applicationContext.contentResolver.query(uri, projection, null, null,
            "$orderBy DESC"
        )
        val columnIndexData = cursor?.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        val thumb = cursor?.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA)
        val time = cursor?.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
        while (cursor!!.moveToNext()) {
            val absolutePathOfImage = cursor.getString(columnIndexData!!)
            val thumbnail = cursor.getString(thumb!!)
            val duration = cursor.getLong(time!!)
            val video = VideoModel(originPath = absolutePathOfImage, thumb = thumbnail, duration = duration)
            mListVideoModel.add(video)
        }

        listVideos.value = mListVideoModel
    }
}