package com.techbmt.hidecalculator.feature.main.saved

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.techbmt.hidecalculator.core.BaseActivity
import com.techbmt.hidecalculator.databinding.ActivitySavedBinding
import com.techbmt.hidecalculator.db.DBViewModel
import com.techbmt.hidecalculator.feature.main.images.AlbumsActivity
import com.techbmt.hidecalculator.feature.main.images.HiddenFile
import com.techbmt.hidecalculator.feature.main.images.ImageAdapter
import com.techbmt.hidecalculator.feature.main.images.ImageHiddenAdapter
import com.techbmt.hidecalculator.feature.main.images.ImagesActivity
import com.techbmt.hidecalculator.feature.main.images.OnClickHiddenImage
import com.techbmt.hidecalculator.feature.main.images.ShowAllHiddenImageActivity
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class SavedActivity : BaseActivity<ActivitySavedBinding>(), OnClickHiddenImage {

    private lateinit var viewModel: DBViewModel
    private var listHiddenImage: List<HiddenFile> = arrayListOf()
    private var mAdapter = ImageHiddenAdapter()

    override fun initView() {
        val file = File("/storage/emulated/0/DCIM/Screenshots/Screenshot_20240106-095739_YouTube.jpg")


        binding.back.setOnClickListener {
            startActivity(Intent(this@SavedActivity, AlbumsActivity::class.java))
        }

        Log.i("ok", file.exists().toString())
        viewModel = ViewModelProvider(this)[DBViewModel::class.java]
        viewModel.readAllHiddenFile.observe(this) {
            listHiddenImage = it
            if(listHiddenImage.isEmpty()) {
                binding.layoutEmpty.visibility = View.VISIBLE
            } else {
                binding.layoutEmpty.visibility = View.GONE
            }
            binding.rcvHiddenImages.apply {
                Log.i("saved", it.toString())
                mAdapter.listener = this@SavedActivity
                mAdapter.submitList(it)
                adapter = mAdapter
            }
        }
    }

    override fun getLayout(): ActivitySavedBinding =
        ActivitySavedBinding.inflate(layoutInflater)

    override fun clickImage(file: HiddenFile, pos: Int) {
        val intent = Intent(this@SavedActivity, ShowAllHiddenImageActivity::class.java)
        intent.putExtra("pos", pos)
        startActivity(intent)
    }


    fun moveFile(location: String, originPath: String) {
        Log.i("name", location)
        val source = File(location)
        val destination = File(originPath)
        Log.i("name2", destination.toString())
        Log.i("exist", source.toString())

        try {
            //Log.i("exist", "OK")
            val src = FileInputStream(source).channel
            val dst = FileOutputStream(destination).channel
            dst.transferFrom(src, 0, src.size())

            src.close()
            dst.close()
            Log.d("moveFile", "File recover......")

        } catch (e: Exception) {
            Log.e("moveFile", e.toString())
        }
        source.delete()
        sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(File(location))))
    }

}