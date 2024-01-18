package com.techbmt.hidecalculator.feature.main.saved

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.techbmt.hidecalculator.R
import com.techbmt.hidecalculator.core.BaseActivity
import com.techbmt.hidecalculator.databinding.ActivityHiddenFileBinding
import com.techbmt.hidecalculator.db.DBViewModel
import com.techbmt.hidecalculator.feature.main.MainActivity
import com.techbmt.hidecalculator.feature.main.images.HiddenFile
import com.techbmt.hidecalculator.feature.main.images.ImageHiddenAdapter
import com.techbmt.hidecalculator.feature.main.images.OnClickHiddenImage
import com.techbmt.hidecalculator.feature.main.images.ShowAllHiddenImageActivity
import com.techbmt.hidecalculator.feature.main.video.OnClickVideo
import com.techbmt.hidecalculator.feature.main.video.PlayVideoActivity
import com.techbmt.hidecalculator.feature.main.video.VideoAdapter
import com.techbmt.hidecalculator.feature.main.video.VideoModel

class HiddenFileActivity : BaseActivity<ActivityHiddenFileBinding>() {

    private lateinit var dbViewModel: DBViewModel
    private var listHiddenImage: List<HiddenFile> = arrayListOf()
    private var hiddenImageAdapter = ImageHiddenAdapter()

    override fun initView() {

        dbViewModel = ViewModelProvider(this)[DBViewModel::class.java]

        binding.back.setOnClickListener {
            startActivity(Intent(this@HiddenFileActivity, MainActivity::class.java))
            finish()
        }
        val mAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            arrayListOf("Image", "Video")
        )
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerType.apply {
            adapter = mAdapter
            onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    p1: View?,
                    pos: Int,
                    p3: Long
                ) {
                    val type = parentView!!.getItemAtPosition(pos).toString()
                    when (type) {
                        "Image" -> {
                            binding.rcvHiddenVideo.visibility = View.GONE
                            binding.rcvHiddenImage.visibility = View.VISIBLE
                            showHiddenImages()
                        }

                        "Video" -> {
                            binding.rcvHiddenImage.visibility = View.GONE
                            binding.rcvHiddenVideo.visibility = View.VISIBLE
                            showHiddenVideos()
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    showHiddenImages()
                }
            }
        }
    }

    private fun showHiddenVideos() {
        dbViewModel.readAllHiddenVideo.observe(this@HiddenFileActivity) {
            if (it.isEmpty()) {
                binding.apply {
                    layoutEmpty.visibility = View.VISIBLE
                }
            } else {

                binding.layoutEmpty.visibility = View.GONE
                val videoAdapter = VideoAdapter(object : OnClickVideo {
                    override fun clickVideo(video: VideoModel) {
                        val intent = Intent(
                            this@HiddenFileActivity,
                            PlayVideoActivity::class.java
                        )
                        val bundle = Bundle()
                        bundle.putSerializable("hidden_video", video)
                        intent.putExtras(bundle)
                        startActivity(intent)
                        finish()
                    }

                    override fun onSecondClick(video: VideoModel) {
                        // do nothing
                    }

                }, 2)
                videoAdapter.submitList(it)
                binding.rcvHiddenVideo.apply {
                    val lm = LinearLayoutManager(this@HiddenFileActivity)
                    lm.orientation = LinearLayoutManager.VERTICAL
                    layoutManager = lm
                    adapter = videoAdapter
                }
            }
        }
    }

    private fun showHiddenImages() {
        dbViewModel.readAllHiddenFile.observe(this@HiddenFileActivity) {
            listHiddenImage = it
            if (listHiddenImage.isEmpty()) {
                binding.layoutEmpty.visibility = View.VISIBLE
            } else {
                binding.layoutEmpty.visibility = View.GONE
            }
            binding.rcvHiddenImage.apply {
                hiddenImageAdapter.listener = object : OnClickHiddenImage {
                    override fun clickImage(file: HiddenFile, pos: Int) {
                        val intent =
                            Intent(this@HiddenFileActivity, ShowAllHiddenImageActivity::class.java)
                        intent.putExtra("pos", pos)
                        startActivity(intent)
                    }
                }
                val lm = GridLayoutManager(this@HiddenFileActivity, 3)
                lm.orientation = LinearLayoutManager.VERTICAL
                layoutManager = lm
                hiddenImageAdapter.submitList(listHiddenImage)
                adapter = hiddenImageAdapter
            }
        }
    }

    override fun getLayout(): ActivityHiddenFileBinding =
        ActivityHiddenFileBinding.inflate(layoutInflater)
}