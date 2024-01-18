package com.techbmt.hidecalculator.feature.user.setting

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.provider.CallLog.Calls.SUBJECT
import com.techbmt.hidecalculator.core.BaseActivity
import com.techbmt.hidecalculator.databinding.ActivitySettingBinding
import com.techbmt.hidecalculator.feature.main.saved.SavedVideoActivity
import com.techbmt.hidecalculator.feature.user.password.SetPasswordActivity
import com.techbmt.hidecalculator.feature.user.setting.language.ChooseLanguageDialog

class SettingActivity : BaseActivity<ActivitySettingBinding>() {
    override fun initView() {
        binding.apply {
//            layoutLanguage.setOnClickListener {
//                val dialog = ChooseLanguageDialog()
//                dialog.show(supportFragmentManager, this@SettingActivity.javaClass.name)
//            }
            back.setOnClickListener {
                finish()
            }
            layoutChangePassword.setOnClickListener {
                val intent = Intent(this@SettingActivity, SetPasswordActivity::class.java)
                startActivity(intent)
            }
            layoutShare.setOnClickListener {
                val sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.type = "text/plan"
                val shareDetails =
                    "https://play.google.com/store/apps/details?id=" + this@SettingActivity.packageName.trim { it <= ' ' }
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT)
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareDetails)
                startActivity(Intent.createChooser(sharingIntent, "share to"))
            }
            layoutRate.setOnClickListener {
                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=${this@SettingActivity.packageName}")
                        )
                    )
                } catch (e: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=${this@SettingActivity.packageName}")
                        )
                    )
                }
            }
        }
    }

    override fun getLayout(): ActivitySettingBinding =
        ActivitySettingBinding.inflate(layoutInflater)


}