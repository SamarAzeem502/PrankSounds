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


    // Full Screen Native Ad
    const val ENABLE_FULL_SCREEN_NATIVE_AD = "enable_full_screen_native_ad"
    const val FULL_SCREEN_NATIVE_AD_ID = "full_screen_native_ad_id"

    //Trimmer Interstitial
    const val TRIMMER_INTER_CAP_CLICKS = "trimmer_inter_cap_clicks"
    const val ENABLE_TRIMMER_INTERSTITIAL_AD = "enable_trimmer_interstitial_ad"
    const val TRIMMER_INTERSTITIAL_AD_ID = "trimmer_interstitial_ad_id"
    const val TRIMMER_INTER_CAP_TYPE = "trimmer_inter_cap_type"
    const val TRIMMER_INTER_LOADER_TIME = "trimmer_inter_loader_time"
    const val TRIMMER_INTER_CAP_TIME = "trimmer_inter_cap_time"

    //Home Interstitial
    const val HOME_INTER_CAP_CLICKS = "home_inter_cap_clicks"
    const val ENABLE_HOME_INTERSTITIAL_AD = "enable_home_interstitial_ad"
    const val HOME_INTERSTITIAL_AD_ID = "home_interstitial_ad_id"
    const val HOME_INTER_CAP_TYPE = "home_inter_cap_type"
    const val HOME_INTER_LOADER_TIME = "home_inter_loader_time"
    const val HOME_INTER_CAP_TIME = "home_inter_cap_time"

    //Fart Interstitial
    const val FART_INTER_CAP_CLICKS = "fart_inter_cap_clicks"
    const val ENABLE_FART_INTERSTITIAL_AD = "enable_fart_interstitial_ad"
    const val FART_INTERSTITIAL_AD_ID = "fart_interstitial_ad_id"
    const val FART_INTER_CAP_TYPE = "fart_inter_cap_type"
    const val FART_INTER_LOADER_TIME = "fart_inter_loader_time"
    const val FART_INTER_CAP_TIME = "fart_inter_cap_time"

    //Vehicle Interstitial
    const val VEHICLE_INTER_CAP_CLICKS = "vehicle_inter_cap_clicks"
    const val ENABLE_VEHICLE_INTERSTITIAL_AD = "enable_vehicle_interstitial_ad"
    const val VEHICLE_INTERSTITIAL_AD_ID = "vehicle_interstitial_ad_id"
    const val VEHICLE_INTER_CAP_TYPE = "vehicle_inter_cap_type"
    const val VEHICLE_INTER_LOADER_TIME = "vehicle_inter_loader_time"
    const val VEHICLE_INTER_CAP_TIME = "vehicle_inter_cap_time"

    //Stunt Gun Interstitial
    const val STUNT_GUN_INTER_CAP_CLICKS = "stunt_gun_inter_cap_clicks"
    const val ENABLE_STUNT_GUN_INTERSTITIAL_AD = "enable_stunt_gun_interstitial_ad"
    const val STUNT_GUN_INTERSTITIAL_AD_ID = "stunt_gun_interstitial_ad_id"
    const val STUNT_GUN_INTER_CAP_TYPE = "stunt_gun_inter_cap_type"
    const val STUNT_GUN_INTER_LOADER_TIME = "stunt_gun_inter_loader_time"
    const val STUNT_GUN_INTER_CAP_TIME = "stunt_gun_inter_cap_time"

    //Halloween Interstitial
    const val HALLOWEEN_INTER_CAP_TIME = "halloween_inter_cap_time"
    const val HALLOWEEN_INTER_CAP_TYPE = "halloween_inter_cap_type"
    const val HALLOWEEN_INTER_CAP_CLICKS = "halloween_inter_cap_clicks"
    const val HALLOWEEN_INTER_LOADER_TIME = "halloween_inter_loader_time"
    const val HALLOWEEN_INTERSTITIAL_AD_ID = "halloween_interstitial_ad_id"
    const val ENABLE_HALLOWEEN_INTERSTITIAL_AD = "enable_halloween_interstitial_ad"

    // Language Selection
    const val LANGUAGE_NATIVE_AD_ID = "language_native_ad_id"
    const val LANGUAGE_NATIVE_AD_TYPE = "language_native_ad_type"
    const val ENABLE_LANGUAGE_NATIVE_AD = "enable_language_native_ad"
    const val LANGUAGE_NATIVE_BUTTON_COLOR = "language_native_button_color"
    const val LANGUAGE_NATIVE_BUTTON_CORNERS = "language_native_button_corners"
    const val LANGUAGE_NATIVE_BUTTON_TEXT_COLOR = "language_native_button_text_color"

    // Main Activity
    const val MAIN_NATIVE_AD_ID = "main_native_ad_id"
    const val MAIN_NATIVE_AD_TYPE = "main_native_ad_type"
    const val ENABLE_MAIN_NATIVE_AD = "enable_main_native_ad"
    const val MAIN_NATIVE_BUTTON_COLOR = "main_native_button_color"
    const val MAIN_NATIVE_BUTTON_CORNERS = "main_native_button_corners"
    const val MAIN_NATIVE_BUTTON_TEXT_COLOR = "main_native_button_text_color"


    // Exit Native Ad
    const val EXIT_NATIVE_AD_ID = "exit_native_ad_id"
    const val EXIT_NATIVE_AD_TYPE = "exit_native_ad_type"
    const val ENABLE_EXIT_NATIVE_AD = "enable_exit_native_ad"
    const val EXIT_NATIVE_BUTTON_COLOR = "exit_native_button_color"
    const val EXIT_NATIVE_BUTTON_ROUND = "exit_native_button_round"
    const val EXIT_NATIVE_BUTTON_TEXT_COLOR = "exit_native_button_text_color"


    //MACHINE1 Activity Banner
    const val MACHINE1_BANNER_AD_ID = "machine1_banner_ad_id"
    const val ENABLE_MACHINE1_BANNER_AD = "enable_machine1_banner_ad"
    const val MACHINE1_BANNER_MAKE_COLLAPSIBLE = "machine1_banner_make_collapsible"

    //MACHINE2 Activity Banner
    const val MACHINE2_BANNER_AD_ID = "machine2_banner_ad_id"
    const val ENABLE_MACHINE2_BANNER_AD = "enable_machine2_banner_ad"
    const val MACHINE2_BANNER_MAKE_COLLAPSIBLE = "machine2_banner_make_collapsible"

    //MACHINE3 Activity Banner
    const val MACHINE3_BANNER_AD_ID = "machine3_banner_ad_id"
    const val ENABLE_MACHINE3_BANNER_AD = "enable_machine3_banner_ad"
    const val MACHINE3_BANNER_MAKE_COLLAPSIBLE = "machine3_banner_make_collapsible"

    //MACHINE4 Activity Banner
    const val MACHINE4_BANNER_AD_ID = "machine4_banner_ad_id"
    const val ENABLE_MACHINE4_BANNER_AD = "enable_machine4_banner_ad"
    const val MACHINE4_BANNER_MAKE_COLLAPSIBLE = "machine4_banner_make_collapsible"

    //MACHINE5 Activity
    const val MACHINE5_BANNER_AD_ID = "machine5_banner_ad_id"
    const val ENABLE_MACHINE5_BANNER_AD = "enable_machine5_banner_ad"
    const val MACHINE5_BANNER_MAKE_COLLAPSIBLE = "machine5_banner_make_collapsible"

    //MACHINE6 Activity
    const val MACHINE6_BANNER_AD_ID = "machine6_banner_ad_id"
    const val ENABLE_MACHINE6_BANNER_AD = "enable_machine6_banner_ad"
    const val MACHINE6_BANNER_MAKE_COLLAPSIBLE = "machine6_banner_make_collapsible"

    //MACHINE7 Activity
    const val MACHINE7_BANNER_AD_ID = "machine7_banner_ad_id"
    const val ENABLE_MACHINE7_BANNER_AD = "enable_machine7_banner_ad"
    const val MACHINE7_BANNER_MAKE_COLLAPSIBLE = "machine7_banner_make_collapsible"

    //MACHINE8 Activity
    const val MACHINE8_BANNER_AD_ID = "machine8_banner_ad_id"
    const val ENABLE_MACHINE8_BANNER_AD = "enable_machine8_banner_ad"
    const val MACHINE8_BANNER_MAKE_COLLAPSIBLE = "machine8_banner_make_collapsible"

    //MACHINE9 Activity
    const val MACHINE9_BANNER_AD_ID = "machine9_banner_ad_id"
    const val ENABLE_MACHINE9_BANNER_AD = "enable_machine9_banner_ad"
    const val MACHINE9_BANNER_MAKE_COLLAPSIBLE = "machine9_banner_make_collapsible"

    //MACHINE10 Activity
    const val MACHINE10_BANNER_AD_ID = "machine10_banner_ad_id"
    const val ENABLE_MACHINE10_BANNER_AD = "enable_machine10_banner_ad"
    const val MACHINE10_BANNER_MAKE_COLLAPSIBLE = "machine10_banner_make_collapsible"

    //MACHINE11 Activity
    const val MACHINE11_BANNER_AD_ID = "machine11_banner_ad_id"
    const val ENABLE_MACHINE11_BANNER_AD = "enable_machine11_banner_ad"
    const val MACHINE11_BANNER_MAKE_COLLAPSIBLE = "machine11_banner_make_collapsible"

    //MACHINE12 Activity
    const val MACHINE12_BANNER_AD_ID = "machine12_banner_ad_id"
    const val ENABLE_MACHINE12_BANNER_AD = "enable_machine12_banner_ad"
    const val MACHINE12_BANNER_MAKE_COLLAPSIBLE = "machine12_banner_make_collapsible"

    //MACHINE13 Activity
    const val MACHINE13_BANNER_AD_ID = "machine13_banner_ad_id"
    const val ENABLE_MACHINE13_BANNER_AD = "enable_machine13_banner_ad"
    const val MACHINE13_BANNER_MAKE_COLLAPSIBLE = "machine13_banner_make_collapsible"

    //AIRHORN Activity
    const val AIRHORN_BANNER_AD_ID = "airhorn_banner_ad_id"
    const val ENABLE_AIRHORN_BANNER_AD = "enable_airhorn_banner_ad"
    const val AIRHORN_BANNER_MAKE_COLLAPSIBLE = "airhorn_banner_make_collapsible"

    //CHANGE_TRIMMER Activity
    const val CHANGE_TRIMMER_BANNER_AD_ID = "change_trimmer_banner_ad_id"
    const val ENABLE_CHANGE_TRIMMER_BANNER_AD = "enable_change_trimmer_banner_ad"
    const val CHANGE_TRIMMER_BANNER_MAKE_COLLAPSIBLE = "change_trimmer_banner_make_collapsible"

    //FART Activity
    const val FART_BANNER_AD_ID = "fart_banner_ad_id"
    const val ENABLE_FART_BANNER_AD = "enable_fart_banner_ad"
    const val FART_BANNER_MAKE_COLLAPSIBLE = "fart_banner_make_collapsible"

    //HALLOWEEN Activity
    const val HALLOWEEN_BANNER_AD_ID = "halloween_banner_ad_id"
    const val ENABLE_HALLOWEEN_BANNER_AD = "enable_halloween_banner_ad"
    const val HALLOWEEN_BANNER_MAKE_COLLAPSIBLE = "halloween_banner_make_collapsible"

    //STUNT GUN Activity
    const val STUNT_GUN_BANNER_AD_ID = "stunt_gun_banner_ad_id"
    const val ENABLE_STUNT_GUN_BANNER_AD = "enable_stunt_gun_banner_ad"
    const val STUNT_GUN_BANNER_MAKE_COLLAPSIBLE = "stunt_gun_banner_make_collapsible"


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
            remoteConfig?.setDefaultsAsync(R.xml.remote_config_defaults)
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
