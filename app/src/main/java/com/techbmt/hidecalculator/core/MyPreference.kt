package com.techbmt.hidecalculator.core

import android.content.Context
import androidx.preference.PreferenceManager

class MyPreference(context: Context) {

    companion object {
        const val USER_PASSWORD = "password"
        const val KEY_LANGUAGE = "app_language"
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

}