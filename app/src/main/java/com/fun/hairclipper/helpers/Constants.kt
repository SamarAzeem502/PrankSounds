package com.`fun`.hairclipper.helpers

import com.`fun`.hairclipper.BuildConfig
import com.`fun`.hairclipper.R

object Constants {
    const val KEY_SELECTED_LANG = "${BuildConfig.APPLICATION_ID}.KEY_SELECTED_LANG"
    const val KEY_IS_FIRST_TIME = "${BuildConfig.APPLICATION_ID}.KEY_IS_FIRST_TIME"
    const val SETTINGS_PREF = "${BuildConfig.APPLICATION_ID}.SETTINGS_PREF"


    val languages
        get() = listOf(
            LanguageModel(R.string.english, "en"),
            LanguageModel(R.string.arabic, "ar"),
            LanguageModel(R.string.french, "fr"),
            LanguageModel(R.string.german, "de"),
            LanguageModel(R.string.indonesia, "id"),
            LanguageModel(R.string.italian, "it"),
            LanguageModel(R.string.malay, "ms"),
            LanguageModel(R.string.pershion, "fa"),
            LanguageModel(R.string.portuguese, "pt"),
            LanguageModel(R.string.russia, "ru"),
            LanguageModel(R.string.spanish, "es"),
            LanguageModel(R.string.turkish, "tr"),
            LanguageModel(R.string.chines, "zh"),
        )

}