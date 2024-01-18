package com.techbmt.hidecalculator.feature.main.video

import android.app.AlertDialog
import android.content.Intent
import android.media.MediaScannerConnection
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.techbmt.hidecalculator.R
import com.techbmt.hidecalculator.core.BaseActivity
import com.techbmt.hidecalculator.databinding.ActivityPlayVideoBinding
import com.techbmt.hidecalculator.db.DBViewModel
import com.techbmt.hidecalculator.feature.main.saved.SavedVideoActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File

class PlayVideoActivity : BaseActivity<ActivityPlayVideoBinding>() {

    private lateinit var dbViewModel: DBViewModel
    private var isCompleted = false
    private var clicked = false

    override fun initView() {
        val hiddenVideo = intent.extras!!.getSerializable("hidden_video") as VideoModel
        dbViewModel = ViewModelProvider(this)[DBViewModel::class.java]
        val controller = MediaController(this@PlayVideoActivity)
        binding.videoView.apply {
            setVideoPath(hiddenVideo.hiddenPath)
            controller.setAnchorView(this)
            setMediaController(controller)
            start()
        }
        binding.apply {
            toolbar.delete.setOnClickListener {
                dbViewModel.deleteHiddenVideo(hiddenVideo.id)
                Handler(mainLooper).postDelayed({
                    val file = File(hiddenVideo.hiddenPath)
                    file.delete()
                }, 200)
            }
            layout.setOnClickListener {
                if (clicked) {
                    binding.toolbar.layout.visibility = View.GONE
                } else {
                    binding.toolbar.layout.visibility = View.VISIBLE
                }
            }
            toolbar.back.setOnClickListener {
                finish()
            }
            toolbar.delete.setOnClickListener {
                dbViewModel.deleteHiddenVideo(hiddenVideo.id)
                val dialog = AlertDialog.Builder(this@PlayVideoActivity)
                dialog.apply {
                    setTitle(getString(R.string.ask_delete_hidden))
                    setMessage(getString(R.string.msg_delete_hidden))
                    setPositiveButton(getString(R.string.delete)) { _, _ ->
                        Handler(mainLooper).postDelayed({
                            dbViewModel.deleteHiddenImage(hiddenVideo.id)
                            val currentFilePath = File(hiddenVideo.hiddenPath)
                            currentFilePath.delete()
                            Toast.makeText(
                                this@PlayVideoActivity,
                                getString(R.string.deleted),
                                Toast.LENGTH_SHORT
                            ).show()
                        }, 500)
                        finish()
                    }
                    setNegativeButton(getString(R.string.cancel)) { _, _ ->
                        // do nothing
                    }
                    show()
                }
            }
            toolbar.eye.setOnClickListener {
                lifecycleScope.launch(Dispatchers.IO) {
                    recoverHiddenVideo(hiddenVideo)
                }

                Toast.makeText(
                    this@PlayVideoActivity,
                    "Recover video successfully",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun recoverHiddenVideo(video: VideoModel) {
        // Đường dẫn đến thư mục ảnh màn hình (Screenshots)
        val destinationFolderPath =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + "/${getDirectoryParentPath(video.originPath)}/"
        Log.i("destination", destinationFolderPath)
        // Tạo đối tượng File cho ảnh nguồn
        val sourceFile = File(getParentPath(video.hiddenPath), getFileNames(video.hiddenPath))

        // Tạo đối tượng File cho thư mục đích
        val destinationFolder = File(destinationFolderPath)

        // Kiểm tra xem thư mục đích có tồn tại không
        if (!destinationFolder.exists()) {
            destinationFolder.mkdirs() // Tạo thư mục nếu chưa tồn tại
        }

        // Tạo đối tượng File cho ảnh đích
        val destinationFile = File(destinationFolderPath, getFileNames(video.hiddenPath))
        Log.i("des", destinationFile.toString())

        // Di chuyển ảnh từ thư mục nguồn đến thư mục đích
        if (sourceFile.renameTo(destinationFile)) {
            // Cập nhật ảnh mới vào thư viện
            MediaScannerConnection.scanFile(
                this@PlayVideoActivity,
                arrayOf(destinationFile.toString()),
                null
            ) { path, uri ->
                isCompleted = true
            }

            // Xoá ảnh từ thư mục nguồn (nếu cần)
            sourceFile.delete()
        }
        dbViewModel.deleteHiddenVideo(video.id)
    }

    private fun getFileNames(path: String): String {
        return File(path).name
    }

    private fun getParentPath(path: String): String {
        return File(path).parent ?: ""
    }

    private fun getDirectoryParentPath(path: String): String {
        val file = File(path)
        val parentFile = file.parentFile
        return parentFile?.name ?: ""
    }

    override fun getLayout(): ActivityPlayVideoBinding {
        return ActivityPlayVideoBinding.inflate(layoutInflater)
    }
}