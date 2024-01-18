package com.techbmt.hidecalculator.feature.main.images

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.File

class ImageViewModel : ViewModel() {

    val listImages = MutableLiveData<List<AlbumModel>>()
    val listImagesApp = MutableLiveData<List<AlbumModel>>()
    private val mListImagesModel = ArrayList<AlbumModel>()
    private var isInsideFolder = false

    fun getImagePath(applicationContext: Context) {
        mListImagesModel.clear()

        var pos = 0
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        var absolutePathOfImage = ""
        val projection =
            arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

        val orderBy = MediaStore.Images.Media.DATE_ADDED
        val cursor = applicationContext.contentResolver.query(
            uri, projection, null, null, "$orderBy DESC"
        )

        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        val columnIndexFolderName =
            cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        viewModelScope.launch {
            while (cursor?.moveToNext() == true) {
                absolutePathOfImage = columnIndex?.let { cursor.getString(it) }.toString()

                for (i in 0 until mListImagesModel.size) {
                    if (mListImagesModel[i].parentFolder == columnIndexFolderName?.let {
                            cursor.getString(
                                it
                            )
                        }) {
                        isInsideFolder = true
                        pos = i
                        break
                    } else {
                        isInsideFolder = false
                    }
                }
                if (isInsideFolder) {
                    val mListImagePath = ArrayList<String>()
                    mListImagePath.addAll(mListImagesModel[pos].path)
                    mListImagePath.add(absolutePathOfImage)
                    mListImagesModel[pos].path = mListImagePath
                } else {
                    val mListImagePath = ArrayList<String>()
                    mListImagePath.add(absolutePathOfImage)
                    val imageModel = columnIndexFolderName?.let { columnIndexFolderName ->
                        cursor.getString(columnIndexFolderName)
                    }?.let {
                        AlbumModel(
                            it, mListImagePath
                        )
                    }
                    if (imageModel != null) {
                        mListImagesModel.add(imageModel)
                        listImages.value = mListImagesModel
                    }
                }
            }
        }
    }

    fun getAllShownImagesPath(context: Context, str: String) {
        val tmp = ArrayList<String>()
        val listImage = ArrayList<AlbumModel>()
        try {
            val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val contentResolver: ContentResolver = context.contentResolver
            val query = contentResolver.query(
                uri,
                null,
                "_data LIKE ? AND mime_type LIKE ? ", arrayOf("%$str%", "%image/%"),
                null
            )
            if (query != null && query.moveToFirst()) {
                do {
                    @SuppressLint("Range") val string =
                        query.getString(query.getColumnIndex("_data"))
                    Uri.fromFile(File(string)).path?.let { tmp.add(it) }
                } while (query.moveToNext())
                query.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        listImage.add(AlbumModel("", tmp))
        listImagesApp.value = listImage
    }

}