package com.`fun`.hairclipper.tools

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

object AppPrefs {
    private const val APP_PREF = "AppPrefs"

    fun getAppTheme(mContext: Context, key: String): Int {
        val preferences = mContext.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE)
        return preferences.getInt(key, AppCompatDelegate.MODE_NIGHT_NO)
    }

    fun setAppTheme(mContext: Context, key: String,value: Int) {
        val preferences = mContext.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE)
        preferences.edit { putInt(key, value) }
    }
}