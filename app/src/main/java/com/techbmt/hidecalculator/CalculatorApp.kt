package com.techbmt.hidecalculator

import android.app.Application
import com.techbmt.hidecalculator.core.MyPreference

class CalculatorApp : Application() {

    private lateinit var prefs: MyPreference

    companion object {
        private lateinit var instance: CalculatorApp

        @JvmStatic
        fun getInstance(): CalculatorApp {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        prefs = MyPreference(this)
        instance = this
        if (!prefs.isFirstInstall()) {
            prefs.setFirstInstall(true)
            prefs.setValueCoin(30)
        }
    }

    fun getMyPreference(): MyPreference {
        return prefs
    }

}