package com.techbmt.hidecalculator.core

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.techbmt.hidecalculator.CalculatorApp
import com.techbmt.hidecalculator.R
import com.techbmt.hidecalculator.feature.calculator.Calculator
import com.techbmt.hidecalculator.feature.user.setting.language.TypeOfLanguage
import java.util.Locale

abstract class BaseActivity<VB: ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getLayout()
        setContentView(binding.root)
        initView()
    }

    abstract fun initView()

    abstract fun getLayout(): VB

    fun getLocale(context: Context): Locale {
        return when(MyPreference(context).getLocale() ?: "") {
            TypeOfLanguage.ENGLISH.toString() -> {
                Locale.ENGLISH
            }
            else -> Locale("vi")
        }
    }

    private fun onAttach(context: Context, locale: Locale? = getLocale(context)): Context {
        return updateLocale(context, locale)
    }


    fun updateLocale(mContext: Context, locale: Locale?): Context {
        val res = mContext.resources
        val config = res.configuration
        Locale.setDefault(locale ?: Locale("vi"))
        Locale.getDefault()
        config.setLocale(locale)
        return mContext.createConfigurationContext(config)
    }

    fun updateLanguage(activity: Activity, locale: Locale? = getLocale(activity.baseContext)) {
        val res = activity.baseContext.resources
        val config = res.configuration
        Locale.setDefault( locale ?: Locale("vi"))
        config.setLocale(locale ?: Locale("vi"))
        activity.onConfigurationChanged(config)
    }


    fun setLocale(locale: Locale?) {
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = locale
        res.updateConfiguration(conf, dm)
        baseContext.resources.updateConfiguration(conf, baseContext.resources.displayMetrics)
        invalidateOptionsMenu()
        onConfigurationChanged(conf)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(onAttach(newBase))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        val newMetrics = resources.displayMetrics
        resources.updateConfiguration(newConfig, newMetrics)
    }

    protected fun showPermissionDenied() {
        AlertDialog.Builder(this).setTitle(R.string.tv_permission_request)
            .setMessage(R.string.go_to_setting_message)
            .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(android.R.string.ok) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:" + this.applicationContext.packageName)
                startActivity(intent)
            }
            .show()
    }

    protected fun isPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val readExternalStoragePermission = if (isAndroid13()) Manifest.permission.READ_MEDIA_IMAGES
            else ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            val writeExternalStoragePermission =
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            readExternalStoragePermission == PackageManager.PERMISSION_GRANTED &&
                    writeExternalStoragePermission == PackageManager.PERMISSION_GRANTED
        }
    }

    fun isAndroid13(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

}