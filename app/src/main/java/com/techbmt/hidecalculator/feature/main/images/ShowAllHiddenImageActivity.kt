package com.techbmt.hidecalculator.feature.main.images

import android.app.AlertDialog
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.techbmt.hidecalculator.R
import com.techbmt.hidecalculator.core.BaseActivity
import com.techbmt.hidecalculator.databinding.ActivityShowAllHiddenImageBinding
import com.techbmt.hidecalculator.db.DBViewModel
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class ShowAllHiddenImageActivity : BaseActivity<ActivityShowAllHiddenImageBinding>() {

    private lateinit var dbViewModel: DBViewModel
    private var clicked = false
    private var listHiddenImage = listOf<HiddenFile>()
    private lateinit var currentFile: HiddenFile


    override fun initView() {


        val pos: Int = intent.extras?.getInt("pos", 0) as Int
        dbViewModel = ViewModelProvider(this)[DBViewModel::class.java]
        binding.apply {

            val handleClick: (HiddenFile, Int) -> Unit = { file, pos ->
                clicked = !clicked
                if (clicked) {
                    toolbar.layout.visibility = View.GONE
                } else {
                    toolbar.layout.visibility = View.VISIBLE
                }
            }

            back.setOnClickListener {
                finish()
            }

            toolbar.back.setOnClickListener {
                finish()
            }

            dbViewModel.readAllHiddenFile.observe(this@ShowAllHiddenImageActivity) {
                val mAdapter = HiddenImageViewPagerAdapter(handleClick)
                Log.i("list_hidden", it.toString())
                listHiddenImage = it
                if (listHiddenImage.isEmpty()) {
                    layoutEmpty.visibility = View.VISIBLE
                    toolbar.layout.visibility = View.GONE
                    vpgHiddenImages.visibility = View.GONE
                    back.visibility = View.VISIBLE
                } else {
                    layoutEmpty.visibility = View.GONE
                    toolbar.layout.visibility = View.VISIBLE
                    vpgHiddenImages.visibility = View.VISIBLE

                    vpgHiddenImages.registerOnPageChangeCallback(object : OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)
                            currentFile = listHiddenImage[position]
                        }
                    })
                    mAdapter.submitList(it)
                    vpgHiddenImages.adapter = mAdapter
                    vpgHiddenImages.currentItem = pos
                    toolbar.eye.setOnClickListener {
                        recoverImage(currentFile)
                    }
                    toolbar.delete.setOnClickListener {
                        val dialog = AlertDialog.Builder(this@ShowAllHiddenImageActivity)
                        dialog.apply {
                            setTitle(getString(R.string.ask_delete_hidden))
                            setMessage(getString(R.string.msg_delete_hidden))
                            setPositiveButton(getString(R.string.delete)) { _, _ ->
                                Handler(mainLooper).postDelayed({
                                    dbViewModel.deleteHiddenImage(currentFile.id)
                                    val currentFilePath = File(currentFile.newPath)
                                    currentFilePath.delete()
                                    Toast.makeText(
                                        this@ShowAllHiddenImageActivity,
                                        getString(R.string.deleted),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    mAdapter.notifyItemRemoved(pos)
                                }, 500)
                            }
                            setNegativeButton(getString(R.string.cancel)) { _, _ ->
                                // do nothing
                            }
                            show()
                        }
                    }
                }
            }
        }
    }

    private fun recoverImage(file: HiddenFile) {
        // Đường dẫn đến thư mục ảnh màn hình (Screenshots)
        val destinationFolderPath =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + "/${getDirectoryParentPath(file.originPath)}/"
        Log.i("destination", destinationFolderPath)
        // Tạo đối tượng File cho ảnh nguồn
        val sourceFile = File(getParentPath(file.newPath), getFileNames(file.newPath))

        // Tạo đối tượng File cho thư mục đích
        val destinationFolder = File(destinationFolderPath)

        // Kiểm tra xem thư mục đích có tồn tại không
        if (!destinationFolder.exists()) {
            destinationFolder.mkdirs() // Tạo thư mục nếu chưa tồn tại
        }

        // Tạo đối tượng File cho ảnh đích
        val destinationFile = File(destinationFolderPath, getFileNames(file.newPath))
        Log.i("des", destinationFile.toString())

        // Di chuyển ảnh từ thư mục nguồn đến thư mục đích
        if (sourceFile.renameTo(destinationFile)) {
            // Cập nhật ảnh mới vào thư viện
            MediaScannerConnection.scanFile(
                this@ShowAllHiddenImageActivity,
                arrayOf(destinationFile.toString()),
                null
            ) { path, uri ->
                // Đã quét xong
            }

            // Xoá ảnh từ thư mục nguồn (nếu cần)
            sourceFile.delete()
        }
        dbViewModel.deleteHiddenImage(file.id)
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

    override fun getLayout(): ActivityShowAllHiddenImageBinding {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        supportActionBar?.hide()
        return ActivityShowAllHiddenImageBinding.inflate(layoutInflater)
    }
}