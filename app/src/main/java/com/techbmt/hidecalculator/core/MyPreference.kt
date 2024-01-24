package com.techbmt.hidecalculator.core

import android.content.Context
import androidx.preference.PreferenceManager

class MyPreference(context: Context) {

    companion object {
        const val USER_PASSWORD = "password"
        const val KEY_LANGUAGE = "app_language"
        const val KEY_TOTAL_COIN = "key_total_coin"
        const val IS_FIRST_INSTALL = "is_first_install"
    }

    private val preference = PreferenceManager.getDefaultSharedPreferences(context)


    fun savePassword(password: String) {
        preference.edit().putString(USER_PASSWORD, password).apply()
    }


    fun getLocale(): String? {
        return preference.getString(KEY_LANGUAGE, "")
    }

    fun saveLocale(language: String) {
        preference.edit().putString(KEY_LANGUAGE, language).apply()
    }

    fun getUserPassword(): String? {
        return preference.getString(USER_PASSWORD, "")
    }

    fun setValueCoin(value: Int) {
        preference.edit().putInt(KEY_TOTAL_COIN, value).apply()
    }

    fun getValueCoin(): Int {
        return preference.getInt(KEY_TOTAL_COIN, 0)
    }

    fun setFirstInstall(value: Boolean) {
        preference.edit().putBoolean(IS_FIRST_INSTALL, value).apply()
    }

    fun isFirstInstall() = preference.getBoolean(IS_FIRST_INSTALL, false)


}