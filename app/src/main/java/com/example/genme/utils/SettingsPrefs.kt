package com.example.genme.utils

import android.content.Context
import android.content.SharedPreferences

object SettingsPrefs {
    private const val FILE = "genme_settings"

    const val KEY_NOTIFICATIONS = "notifications_enabled"
    const val KEY_USE_CELLULAR = "use_cellular"
    const val KEY_HIGH_QUALITY = "high_quality_saves"
    const val KEY_DISPLAY_NAME = "display_name"

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE)

    fun getBool(context: Context, key: String, def: Boolean = false): Boolean =
        prefs(context).getBoolean(key, def)

    fun setBool(context: Context, key: String, value: Boolean) {
        prefs(context).edit().putBoolean(key, value).apply()
    }

    fun getString(context: Context, key: String, def: String = ""): String =
        prefs(context).getString(key, def) ?: def

    fun setString(context: Context, key: String, value: String) {
        prefs(context).edit().putString(key, value).apply()
    }
}

