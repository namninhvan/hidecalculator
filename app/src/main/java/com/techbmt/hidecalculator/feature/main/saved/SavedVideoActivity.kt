package com.techbmt.hidecalculator.feature.main.saved

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.techbmt.hidecalculator.core.BaseActivity
import com.techbmt.hidecalculator.databinding.ActivitySavedVideoBinding
import com.techbmt.hidecalculator.db.DBViewModel
import com.techbmt.hidecalculator.feature.main.video.OnClickVideo
import com.techbmt.hidecalculator.feature.main.video.PlayVideoActivity
import com.techbmt.hidecalculator.feature.main.video.VideoAdapter
import com.techbmt.hidecalculator.feature.main.video.VideoModel

class SavedVideoActivity : BaseActivity<ActivitySavedVideoBinding>() {

    private lateinit var dbViewModel: DBViewModel

    override fun initView() {
        dbViewModel = ViewModelProvider(this)[DBViewModel::class.java]
        dbViewModel.readAllHiddenVideo.observe(this@SavedVideoActivity) {
            if (it.isEmpty()) {
                binding.apply {
                    layoutEmpty.visibility = View.VISIBLE
                }
            } else {
                binding.layoutEmpty.visibility = View.GONE
                val mAdapter = VideoAdapter(object : OnClickVideo {
                    override fun clickVideo(video: VideoModel) {
                        val intent = Intent(this@SavedVideoActivity, PlayVideoActivity::class.java)
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
                mAdapter.submitList(it)
                binding.rcvVideos.adapter = mAdapter
            }
        }
    }

    override fun getLayout(): ActivitySavedVideoBinding {
        return ActivitySavedVideoBinding.inflate(layoutInflater)
    }
}