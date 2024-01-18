package com.techbmt.hidecalculator.feature.main.video

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.techbmt.hidecalculator.core.BaseActivity
import com.techbmt.hidecalculator.databinding.ActivityVideoBinding
import com.techbmt.hidecalculator.db.DBViewModel
import com.techbmt.hidecalculator.feature.main.images.HiddenFile
import com.techbmt.hidecalculator.feature.main.images.ImagesActivity
import com.techbmt.hidecalculator.feature.main.saved.HiddenFileActivity
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class VideoActivity : BaseActivity<ActivityVideoBinding>() {

    private lateinit var videoViewModel: VideoViewModel
    private lateinit var dbViewModel: DBViewModel
    private lateinit var mAdapter: VideoAdapter
    private var listSelectedVideos: ArrayList<VideoModel> = arrayListOf()
    private var listSelectedVideosOriginPath: ArrayList<String> = arrayListOf()

    @SuppressLint("NotifyDataSetChanged")
    override fun initView() {
        generateSecretMain()
        videoViewModel = ViewModelProvider(this)[VideoViewModel::class.java]
        dbViewModel = ViewModelProvider(this)[DBViewModel::class.java]
        binding.back.setOnClickListener {
            finish()
        }
        videoViewModel.apply {
            getVideoPath(this@VideoActivity)
            listVideos.observe(this@VideoActivity) {
                if (it.isEmpty()) {
                    binding.layoutEmpty.visibility = View.VISIBLE
                    binding.rcvVideos.visibility = View.GONE
                } else {
                    binding.layoutEmpty.visibility = View.GONE
                    binding.rcvVideos.visibility = View.VISIBLE
                    mAdapter = VideoAdapter(object : OnClickVideo {
                        override fun clickVideo(video: VideoModel) {
                            if (!listSelectedVideos.contains(video)) {
                                listSelectedVideos.add(video)
                            }
                        }

                        override fun onSecondClick(video: VideoModel) {
                            if (listSelectedVideos.contains(video)) {
                                listSelectedVideos.remove(video)
                            }
                        }

                    }, 1)
                    mAdapter.submitList(it)
                    binding.rcvVideos.adapter = mAdapter
                }
            }
            binding.ivAdd.setOnClickListener {
                if (listSelectedVideos.isEmpty()) {
                    Toast.makeText(
                        this@VideoActivity,
                        "Select video needed to hide",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    for (i in listSelectedVideos.indices) {
                        moveVideoFile(
                            listSelectedVideos[i],
                            getVideoFileName(listSelectedVideos[i].originPath)
                        )
                        startActivity(Intent(this@VideoActivity, HiddenFileActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }

    private fun getVideoFileName(path: String): String {
        return File(path).name
    }

    private fun getDataDir(context: Context): String? {
        try {
            return context.packageManager.getPackageInfo(
                context.packageName,
                0
            ).applicationInfo.dataDir
        } catch (e: Exception) {
            Log.e("data_dir", "getDataDir: No Dir")
        }
        return null
    }

    private fun generateSecretMain() {
        try {
            val root = File(
                Environment.getExternalStorageDirectory()
                    .toString() + getDataDir(this) + ImagesActivity.MAIN_DIR
            )
            if (!root.exists()) {
                root.mkdirs()
            }
        } catch (e: Exception) {
            Log.e("generateSecretMain", e.toString())
        }
    }

    private fun moveVideoFile(video: VideoModel, name: String) {
        val source = File(video.originPath)
        val destination = File(
            Environment.getExternalStorageDirectory()
                .toString() + getDataDir(this) + ImagesActivity.MAIN_DIR,
            name
        )
        Log.i("123", destination.toString())
        Log.i("1234", Environment.getExternalStorageDirectory().toString())
        Log.i("1235", getDataDir(this).toString())
        Log.i("1236", ImagesActivity.MAIN_DIR)
        Log.i("1237", name)

        try {
            if (source.exists()) {
                val src = FileInputStream(source).channel
                val dst = FileOutputStream(destination).channel
                dst.transferFrom(src, 0, src.size())

                src.close()
                dst.close()
                Log.d("moveFile", "File moved......")
            }
        } catch (e: Exception) {
            Log.e("moveFile", e.toString())
        }
        source.delete()
        sendBroadcast(
            Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(File(video.originPath))
            )
        )

        dbViewModel.addHiddenVideo(
            VideoModel(
                0, video.originPath,
                Environment.getExternalStorageDirectory()
                    .toString() + getDataDir(this) + ImagesActivity.MAIN_DIR + name,
                thumb = video.thumb, duration = video.duration
            )
        )

    }


    override fun getLayout(): ActivityVideoBinding =
        ActivityVideoBinding.inflate(layoutInflater)
}