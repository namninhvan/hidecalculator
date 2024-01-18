package com.techbmt.hidecalculator.feature.main

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.techbmt.hidecalculator.R
import com.techbmt.hidecalculator.core.BaseActivity
import com.techbmt.hidecalculator.core.OnItemClickListener
import com.techbmt.hidecalculator.databinding.ActivityMainBinding
import com.techbmt.hidecalculator.feature.main.images.AlbumsActivity
import com.techbmt.hidecalculator.feature.main.note.NoteActivity
import com.techbmt.hidecalculator.feature.main.saved.HiddenFileActivity
import com.techbmt.hidecalculator.feature.main.saved.SavedActivity
import com.techbmt.hidecalculator.feature.main.video.VideoActivity
import com.techbmt.hidecalculator.feature.user.setting.SettingActivity


class MainActivity : BaseActivity<ActivityMainBinding>() {

    private var listFeature: ArrayList<FeatureItem> = arrayListOf()

    override fun initView() {

        listFeature.add(FeatureItem(1, getString(R.string.image), R.drawable.circle_image))
        listFeature.add(FeatureItem(2, getString(R.string.video), R.drawable.circle_video))
        listFeature.add(FeatureItem(3, getString(R.string.file), R.drawable.circle_file))
        listFeature.add(FeatureItem(4, getString(R.string.note), R.drawable.circle_note))

        binding.apply {
            setting.setOnClickListener {
                startActivity(Intent(this@MainActivity, SettingActivity::class.java))
            }


            rcvFeature.adapter = MainFeatureAdapter(listFeature, object : OnItemClickListener {
                @RequiresApi(Build.VERSION_CODES.R)
                override fun onItemClick(data: Any) {
                    val feature = data as FeatureItem
                    when (feature.id) {
                        4 -> {
                            startActivity(Intent(this@MainActivity, NoteActivity::class.java))
                        }

                        1 -> {
                            if (SDK_INT >= Build.VERSION_CODES.R) {
                                if (Environment.isExternalStorageManager()) {
                                    startActivity(
                                        Intent(
                                            this@MainActivity,
                                            AlbumsActivity::class.java
                                        )
                                    )
                                } else { //request for the permission
                                    val intent =
                                        Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                                    val uri = Uri.fromParts("package", packageName, null)
                                    intent.setData(uri)
                                    startActivity(intent)
                                }
                            } else {
                                //below android 11=======
                                startActivity(Intent(this@MainActivity, AlbumsActivity::class.java))
                                ActivityCompat.requestPermissions(
                                    this@MainActivity,
                                    arrayOf<String>(WRITE_EXTERNAL_STORAGE),
                                    101
                                )
                            }
                        }

                        2 -> {
                            if (SDK_INT >= Build.VERSION_CODES.R) {
                                if (Environment.isExternalStorageManager()) {
                                    startActivity(
                                        Intent(
                                            this@MainActivity,
                                            VideoActivity::class.java
                                        )
                                    )
                                } else { //request for the permission
                                    val intent =
                                        Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                                    val uri = Uri.fromParts("package", packageName, null)
                                    intent.setData(uri)
                                    startActivity(intent)
                                }
                            } else {
                                //below android 11=======
                                startActivity(Intent(this@MainActivity, VideoActivity::class.java))
                                ActivityCompat.requestPermissions(
                                    this@MainActivity,
                                    arrayOf<String>(WRITE_EXTERNAL_STORAGE),
                                    101
                                )
                            }
                        }

                        3 -> {
                            startActivity(Intent(this@MainActivity, HiddenFileActivity::class.java))
                        }
                    }
                }
            })
        }
    }

    private fun isManagedFileIsGranted(clazz: Class<*>) {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                startActivity(
                    Intent(
                        this@MainActivity,
                        clazz
                    )
                )
            } else { //request for the permission
                val intent =
                    Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                val uri = Uri.fromParts("package", packageName, null)
                intent.setData(uri)
                startActivity(intent)
            }
        } else {
            //below android 11=======
            startActivity(Intent(this@MainActivity, AlbumsActivity::class.java))
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf<String>(WRITE_EXTERNAL_STORAGE),
                101
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@MainActivity, "OK", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this@MainActivity, "NO", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun getLayout(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }
}