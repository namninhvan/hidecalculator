package com.techbmt.hidecalculator.feature.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.util.Log
import com.techbmt.hidecalculator.feature.calculator.CalculatorActivity
import com.techbmt.hidecalculator.core.BaseActivity
import com.techbmt.hidecalculator.core.MyPreference
import com.techbmt.hidecalculator.databinding.ActivitySplashBinding
import com.techbmt.hidecalculator.feature.user.password.SetPasswordActivity
import com.techbmt.hidecalculator.feature.user.setting.language.TypeOfLanguage
import java.lang.reflect.Type

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>() {


    override fun initView() {
        MyPreference(this).saveLocale(TypeOfLanguage.ENGLISH.toString())
        if (MyPreference(this).getUserPassword() == "") {
            Handler(mainLooper).postDelayed({ goToSetPasswordScreen() }, 2500)
        } else {
            Handler(mainLooper).postDelayed({ goToCalculatorActivity() }, 2500)
        }
    }

    private fun goToCalculatorActivity() {
        val intent = Intent(this, CalculatorActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToSetPasswordScreen() {
        val intent = Intent(this, SetPasswordActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun getLayout(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }


}