package com.techbmt.hidecalculator.feature.main.images

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.techbmt.hidecalculator.core.BaseActivity
import com.techbmt.hidecalculator.databinding.ActivityImagesBinding
import com.techbmt.hidecalculator.db.DBViewModel
import com.techbmt.hidecalculator.feature.main.saved.HiddenFileActivity
import com.techbmt.hidecalculator.feature.main.saved.SavedActivity
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class ImagesActivity : BaseActivity<ActivityImagesBinding>(), OnClickImage {

    companion object {
        const val MAIN_DIR = "/.HideCalculator/"
    }

    private lateinit var imageViewModel: ImageViewModel
    private lateinit var dbViewModel: DBViewModel
    private var listImageSelected = arrayListOf<String>()
    private val imageAdapter = ImageAdapter()
    private lateinit var album: AlbumModel
    private val headPath = ""
    private var indexFolder = 0

    override fun initView() {
        imageViewModel = ViewModelProvider(this)[ImageViewModel::class.java]
        dbViewModel = ViewModelProvider(this)[DBViewModel::class.java]
        imageAdapter.listener = this
        generateSecretMain()
        if (intent != null) {
            indexFolder = intent.extras!!.getInt(AlbumsActivity.INDEX_FOLDER, 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                album =
                    intent.extras!!.getSerializable(AlbumsActivity.ALBUM, AlbumModel::class.java)!!
            } else {
                album = (intent.extras!!.getSerializable(AlbumsActivity.ALBUM) as AlbumModel?)!!
            }
            binding.title.text = album.parentFolder
            imageViewModel.apply {
                getImagePath(this@ImagesActivity)
                listImages.observe(this@ImagesActivity) { listImages ->
                    imageAdapter.submitList(listImages[indexFolder].path)
                    Log.i("size", listImages.size.toString())
                }
                binding.rcvImages.adapter = imageAdapter
            }
        }
        binding.back.setOnClickListener {
            startActivity(Intent(this@ImagesActivity, AlbumsActivity::class.java))
            finish()
        }


        binding.ivAdd.setOnClickListener {
            for (i in 0 until listImageSelected.size) {
                moveImageFile(listImageSelected[i], getFileNames(listImageSelected)[i])
            }
            Log.i("list", listImageSelected.toString())
            val intent = Intent(this, HiddenFileActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Hidden successfully", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun getLayout(): ActivityImagesBinding {
        return ActivityImagesBinding.inflate(layoutInflater)
    }

    override fun clickImage(path: String) {
        if (!listImageSelected.contains(path)) {
            listImageSelected.add(path)
        }
    }

    override fun onSecondClick(path: String) {
        if (listImageSelected.contains(path)) {
            listImageSelected.remove(path)
        }
    }


    private fun getFileNames(selected: ArrayList<String>): ArrayList<String> {
        val nameImages = ArrayList<String>()
        for (i in 0 until listImageSelected.size) {
            val name = selected[i].trim()
            val a = selected[i].lastIndexOf("/")
            val b = selected[i].length
            val newName = name.substring(a + 1, b)
            nameImages.add(newName)
        }
        return nameImages
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
                Environment.getExternalStorageDirectory().toString() + getDataDir(this) + MAIN_DIR
            )
            if (!root.exists()) {
                root.mkdirs()
            }
        } catch (e: Exception) {
            Log.e("generateSecretMain", e.toString())
        }
    }

    private fun moveImageFile(location: String, name: String) {
        val source = File(location)
        val destination = File(
            Environment.getExternalStorageDirectory().toString() + getDataDir(this) + MAIN_DIR,
            name
        )

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
        sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(File(location))))

        dbViewModel.addHiddenFile(
            HiddenFile(
                0, 1, location,
                Environment.getExternalStorageDirectory()
                    .toString() + getDataDir(this) + MAIN_DIR + name
            )
        )

    }

}