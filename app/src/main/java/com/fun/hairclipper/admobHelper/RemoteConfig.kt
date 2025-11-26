package com.`fun`.hairclipper.admobHelper

import android.annotation.SuppressLint
import android.util.Log
import com.`fun`.hairclipper.R
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

object RemoteConfig {

    //Splash Ad
    const val SPLASH_BANNER_AD_ID = "splash_banner_ad_id"
    const val ENABLE_SPLASH_BANNER_AD = "enable_splash_banner_ad"
    const val SPLASH_INTERSTITIAL_AD_ID = "splash_interstitial_ad_id"
    const val ENABLE_SPLASH_INTERSTITIAL_AD = "enable_splash_interstitial_ad"

    const val ENABLE_FULL_SCREEN_NATIVE_AD = "enable_full_screen_native_ad"
    const val FULL_SCREEN_NATIVE_AD_ID = "full_screen_native_ad_id"




    // Interstitial Ad
    const val APP_INTER_CAP_CLICKS = "app_inter_cap_clicks"
    const val ENABLE_APP_INTERSTITIAL_AD = "enable_app_interstitial_ad"
    const val ENABLE_FIX_INTERSTITIAL_AD = "enable_fix_interstitial_ad"
    const val FIX_INTERSTITIAL_AD_ID = "fix_interstitial_ad_id"
    const val APP_INTER_CAP_TYPE = "app_inter_cap_type"
    const val APP_INTER_LOADER_TIME = "app_inter_loader_time"
    const val APP_INTERSTITIAL_AD_ID = "app_interstitial_ad_id"
    const val APP_INTER_CAP_TIME = "app_inter_cap_time"

    // Language Selection
    const val LANGUAGE_NATIVE_AD_ID = "language_native_ad_id"
    const val LANGUAGE_NATIVE_AD_TYPE = "language_native_ad_type"
    const val ENABLE_LANGUAGE_NATIVE_AD = "enable_language_native_ad"
    const val LANGUAGE_NATIVE_BUTTON_COLOR = "language_native_button_color"
    const val LANGUAGE_NATIVE_BUTTON_CORNERS = "language_native_button_corners"
    const val LANGUAGE_NATIVE_BUTTON_TEXT_COLOR = "language_native_button_text_color"
    const val LANGUAGE_BANNER_AD_ID = "language_banner_ad_id"
    const val ENABLE_LANGUAGE_BANNER = "enable_language_banner"

    //Activate Activity
    const val ACTIVATE_NATIVE_AD_ID = "activate_native_ad_id"
    const val ACTIVATE_NATIVE_AD_TYPE = "activate_native_ad_type"
    const val ENABLE_ACTIVATE_NATIVE_AD = "enable_activate_native_ad"
    const val ACTIVATE_NATIVE_BUTTON_COLOR = "activate_native_button_color"
    const val ACTIVATE_NATIVE_BUTTON_CORNERS = "activate_native_button_corners"
    const val ACTIVATE_NATIVE_BUTTON_TEXT_COLOR = "activate_native_button_text_color"

    // Intro Screen
    const val INTRO_AD_TYPE = "intro_ad_type"
    const val INTRO_NATIVE_AD_ID = "intro_native_ad_id"
    const val INTRO_NATIVE_AD_TYPE = "intro_native_ad_type"
    const val ENABLE_INTRO_NATIVE_AD = "enable_intro_native_ad"
    const val INTRO_NATIVE_BUTTON_COLOR = "intro_native_button_color"
    const val INTRO_NATIVE_BUTTON_CORNERS = "intro_native_button_corners"
    const val INTRO_NATIVE_BUTTON_TEXT_COLOR = "intro_native_button_text_color"
    const val INTRO_FULL_SCREEN_NATIVE_AD_ID = "intro_full_screen_native_ad_id"
    const val ENABLE_FULL_SCREEN_INTRO_NATIVE_AD = "enable_full_screen_intro_native_ad"

    // Main Activity
    const val MAIN_NATIVE_AD_ID = "main_native_ad_id"
    const val MAIN_NATIVE_AD_TYPE = "main_native_ad_type"
    const val ENABLE_MAIN_NATIVE_AD = "enable_main_native_ad"
    const val MAIN_NATIVE_AD_LOCATION = "main_native_ad_location"
    const val MAIN_RECYCLER_VIEW_TYPE = "main_recycler_view_type"
    const val MAIN_NATIVE_BUTTON_COLOR = "main_native_button_color"
    const val MAIN_NATIVE_BUTTON_CORNERS = "main_native_button_corners"
    const val MAIN_NATIVE_BUTTON_TEXT_COLOR = "main_native_button_text_color"

    // Sound Activity
    const val SOUND_NATIVE_AD_ID = "sound_native_ad_id"
    const val SOUND_NATIVE_AD_TYPE = "sound_native_ad_type"
    const val ENABLE_SOUND_NATIVE_AD = "enable_sound_native_ad"
    const val SOUND_NATIVE_AD_LOCATION = "sound_native_ad_location"
    const val SOUND_NATIVE_BUTTON_COLOR = "sound_native_button_color"
    const val SOUND_NATIVE_BUTTON_CORNERS = "sound_native_button_corners"
    const val SOUND_NATIVE_BUTTON_TEXT_COLOR = "sound_native_button_text_color"

    // Exit Native Ad
    const val EXIT_BANNER_AD_ID = "exit_banner_ad_id"
    const val ENABLE_EXIT_BANNER_AD = "enable_exit_banner_ad"
    const val EXIT_NATIVE_AD_ID = "exit_native_ad_id"
    const val EXIT_NATIVE_AD_TYPE = "exit_native_ad_type"
    const val ENABLE_EXIT_NATIVE_AD = "enable_exit_native_ad"
    const val EXIT_NATIVE_BUTTON_COLOR = "exit_native_button_color"
    const val EXIT_NATIVE_BUTTON_ROUND = "exit_native_button_round"
    const val EXIT_NATIVE_BUTTON_TEXT_COLOR = "exit_native_button_text_color"

    // HTU Native Ad
    const val HTU_NATIVE_AD_ID = "htu_native_ad_id"
    const val HTU_NATIVE_AD_TYPE = "htu_native_ad_type"
    const val ENABLE_HTU_NATIVE_AD = "enable_htu_native_ad"
    const val HTU_NATIVE_BUTTON_COLOR = "htu_native_button_color"
    const val HTU_NATIVE_BUTTON_ROUND = "htu_native_button_round"
    const val HTU_NATIVE_BUTTON_TEXT_COLOR = "htu_native_button_text_color"

    // Main Activity Banner
    const val MAIN_BANNER_AD_ID = "main_banner_ad_id"
    const val ENABLE_MAIN_BANNER_AD = "enable_main_banner_ad"
    const val MAIN_BANNER_MAKE_COLLAPSIBLE = "main_banner_make_collapsible"

    // Settings Activity Banner
    const val SETTINGS_BANNER_AD_ID = "settings_banner_ad_id"
    const val ENABLE_SETTINGS_BANNER_AD = "enable_settings_banner_ad"
    const val SETTINGS_BANNER_MAKE_COLLAPSIBLE = "settings_banner_make_collapsible"

    // Sounds Activity Banner
    const val SOUND_BANNER_AD_ID = "sound_banner_ad_id"
    const val ENABLE_SOUND_BANNER_AD = "enable_sound_banner_ad"
    const val SOUND_BANNER_MAKE_COLLAPSIBLE = "sound_banner_make_collapsible"

    // Settings Activity
    const val SETTING_NATIVE_AD_ID = "setting_native_ad_id"
    const val SETTING_NATIVE_AD_TYPE = "setting_native_ad_type"
    const val ENABLE_SETTING_NATIVE_AD = "enable_setting_native_ad"
    const val SETTING_NATIVE_BUTTON_COLOR = "setting_native_button_color"
    const val SETTING_NATIVE_BUTTON_CORNERS = "setting_native_button_corners"
    const val SETTING_NATIVE_BUTTON_TEXT_COLOR = "setting_native_button_text_color"

    // Premium Activity
    const val PRO_INTERSTITIAL_AD_ID = "pro_interstitial_ad_id"
    const val ENABLE_PRO_INTERSTITIAL_AD = "enable_pro_interstitial_ad"

    //Rewarded Ad
    const val REWARDED_INTERSTITIAL_AD_ID = "rewarded_interstitial_ad_id"

    @SuppressLint("StaticFieldLeak")
    private var remoteConfig: FirebaseRemoteConfig? = null

    @JvmStatic
    fun getRemoteConfig(): FirebaseRemoteConfig {
        if (remoteConfig == null) {
            remoteConfig = FirebaseRemoteConfig.getInstance()
            val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(5)
                .build()
            remoteConfig?.setConfigSettingsAsync(configSettings)
//            remoteConfig?.setDefaultsAsync(R.xml.remote_config_defaults)
            remoteConfig?.addOnConfigUpdateListener(object : ConfigUpdateListener {
                override fun onUpdate(configUpdate: ConfigUpdate) {
                    remoteConfig?.activate()
                }

                override fun onError(error: FirebaseRemoteConfigException) {
                    Log.e("RemoteConfig", "Error updating remote config: ${error.message}")
                }
            })
            remoteConfig?.fetchAndActivate()
        }
        return remoteConfig!!
    }

    fun getBoolean(key: String): Boolean {
        return getRemoteConfig().getBoolean(key)
    }

    fun getString(key: String): String {
        return getRemoteConfig().getString(key)
    }

    fun getLong(key: String): Long {
        return getRemoteConfig().getLong(key)
    }
}
