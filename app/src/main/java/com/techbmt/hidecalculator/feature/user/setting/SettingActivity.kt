package com.techbmt.hidecalculator.feature.user.setting

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.provider.CallLog.Calls.SUBJECT
import android.widget.Toast
import com.techbmt.hidecalculator.core.BaseActivity
import com.techbmt.hidecalculator.core.MyPreference
import com.techbmt.hidecalculator.databinding.ActivitySettingBinding
import com.techbmt.hidecalculator.feature.main.saved.SavedVideoActivity
import com.techbmt.hidecalculator.feature.user.password.SetPasswordActivity
import com.techbmt.hidecalculator.feature.user.setting.language.ChooseLanguageDialog

class SettingActivity : BaseActivity<ActivitySettingBinding>() {
    private var currentCoin = 0
    private lateinit var pref: MyPreference
    override fun initView() {
        pref = MyPreference(this)
        binding.apply {
//            layoutLanguage.setOnClickListener {
//                val dialog = ChooseLanguageDialog()
//                dialog.show(supportFragmentManager, this@SettingActivity.javaClass.name)
//            }
            back.setOnClickListener {
                finish()
            }

            tvCoin.text = pref.getValueCoin().toString()
            currentCoin = pref.getValueCoin()

            layoutChangePassword.setOnClickListener {
                val alertDialog = AlertDialog.Builder(this@SettingActivity)
                alertDialog.apply {
                    setTitle("Purchase confirmation")
                    setMessage("Are you sure you want to pay 2 coin to use this feature?")
                    setPositiveButton(
                        "Yes"
                    ) { dialogInterface, which ->
                        if (currentCoin >= 2) {
                            currentCoin -= 2
                            tvCoin.text = currentCoin.toString()
                            pref.setValueCoin(currentCoin)
                            val intent = Intent(this@SettingActivity, SetPasswordActivity::class.java)
                            startActivity(intent)

                        } else {
                            Toast.makeText(
                                context,
                                "You do not have enough coin, please top up to use this feature",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    setNegativeButton(
                        "No"
                    ) { dialog, which ->
                        dialog.dismiss()
                    }
                }

                val dialog = alertDialog.create()
                dialog.show()

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