package com.example.sahayak

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import java.util.Locale

// This object holds all the language-switching logic
object LocaleHelper {

    private const val PREFS_NAME = "SeniorCareApp"
    private const val PREF_KEY_LANGUAGE = "SELECTED_LANGUAGE"

    // This tag makes the function visible to Java
    @JvmStatic
    fun onAttach(context: Context): Context {
        val lang = getSavedLocale(context)
        return setLocale(context, lang)
    }

    // This tag makes the function visible to Java
    @JvmStatic
    fun setLocale(context: Context) {
        val lang = getSavedLocale(context)
        setLocale(context, lang)
    }

    // This tag makes the function visible to Java
    @JvmStatic
    fun saveLocale(context: Context, lang: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(PREF_KEY_LANGUAGE, lang).apply()
    }

    private fun getSavedLocale(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(PREF_KEY_LANGUAGE, "en") ?: "en" // Default to English
    }

    private fun setLocale(context: Context, lang: String): Context {
        val locale = Locale(lang)
        Locale.setDefault(locale)

        val res: Resources = context.resources
        val config: Configuration = Configuration(res.configuration)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
            context.createConfigurationContext(config)
        } else {
            config.locale = locale
            res.updateConfiguration(config, res.getDisplayMetrics())
            context
        }
    }
}