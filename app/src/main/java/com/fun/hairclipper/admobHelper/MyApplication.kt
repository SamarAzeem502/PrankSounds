package com.`fun`.hairclipper.admobHelper

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.graphics.drawable.toDrawable
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.`fun`.hairclipper.R
import com.`fun`.hairclipper.admobHelper.AdConstants.appOpenToInterstitialCapComplete
import com.`fun`.hairclipper.databinding.ProgressDailogBinding
import com.`fun`.hairclipper.helpers.AppPrefs
import com.`fun`.hairclipper.helpers.LocaleNow
import com.`fun`.hairclipper.helpers.PaymentSubscriptionKotlin
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.MobileAds
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics

class MyApplication : MultiDexApplication() {
    val paymentSubscription: PaymentSubscriptionKotlin by lazy { PaymentSubscriptionKotlin(this) }

    override fun onCreate() {
        super.onCreate()
        try {
            MobileAds.initialize(this)
        } catch (_: Exception) {
        }
        AppCompatDelegate.setDefaultNightMode(AppPrefs.getAppTheme(this, "app_theme"))
        if (paymentSubscription.isPurchased.not() && internetConnection(this)) {
            AppOpenManager(this)
        }
    }

    companion object {

        // ========== HOME INTERSTITIAL ==========
        private var clickCapHome = 0
        private var timeCapHome = true

        fun resetInterstitialHomeTimeCap() {
            timeCapHome = false
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    timeCapHome = true
                },
                RemoteConfig.getRemoteConfig().getLong(RemoteConfig.HOME_INTER_CAP_TIME.trim())
            )
        }

        fun showInterstitialAdHome(
            activity: Activity,
            fullScreenContentCallback: FullScreenAdListener? = null,
            fromActivity: String,
        ) {
            val from = fromActivity.lowercase()

            if (AdConstants.INTERSTITIAL_AD_LOADED.not() || internetConnection(activity).not() ||
                appOpenToInterstitialCapComplete.not() || activity.paymentSubscriptionLazy().value.isPurchased
            ) {
                fullScreenContentCallback?.gotoNext()
                return
            }

            clickCapHome++
            val capTypeHome = RemoteConfig.getString(RemoteConfig.HOME_INTER_CAP_TYPE).trim()

            if (capTypeHome == "time".trim() && timeCapHome.not()) {
                Log.d("capping_home", "Cap type: time | blocked due to timer not complete")
                fullScreenContentCallback?.gotoNext()
                return
            }

            if (capTypeHome == "clicks".trim() && clickCapHome < RemoteConfig.getRemoteConfig()
                    .getLong(RemoteConfig.HOME_INTER_CAP_CLICKS)
            ) {
                Log.d("capping_home", "Cap type: clicks | click count not reached yet")
                fullScreenContentCallback?.gotoNext()
                return
            }

            AdConstants.SHOWING_INTER_AD = true
            showProgress(activity)

            Handler(Looper.getMainLooper()).postDelayed({
                hideProgress()
                InterstitialHomeActivity.showInterstitialAdHome(
                    activity,
                    object : FullScreenAdListener() {
                        override fun onAdDismissedFullScreenContent() {
                            resetInterstitialHomeTimeCap()
                            analytics.logEvent("${from}_interstitial_home_dismissed", null)
                        }

                        override fun onAdFailedToShowFullScreenContent(var1: AdError) {
                            val adId =
                                RemoteConfig.getString(RemoteConfig.HOME_INTERSTITIAL_AD_ID).trim()
                            InterstitialHomeActivity.loadInterstitialAdHome(
                                activity.applicationContext,
                                adId
                            )
                            analytics.logEvent("${from}_interstitial_home_fail_show", null)
                        }

                        override fun onAdShowedFullScreenContent() {
                            timeCapHome = false
                            clickCapHome = 0
                            val adId =
                                RemoteConfig.getString(RemoteConfig.HOME_INTERSTITIAL_AD_ID).trim()
                            InterstitialHomeActivity.loadInterstitialAdHome(
                                activity.applicationContext,
                                if (AdConstants.TEST_ADS) activity.getString(R.string.Interstitial) else adId
                            )
                            analytics.logEvent("${from}_interstitial_home_showed", null)
                        }

                        override fun onAdClicked() {
                            super.onAdClicked()
                            analytics.logEvent("${from}_interstitial_home_clicked", null)
                        }

                        override fun onAdImpression() {
                            super.onAdImpression()
                            analytics.logEvent("${from}_interstitial_home_impression", null)
                        }

                        override fun gotoNext() {
                            fullScreenContentCallback?.gotoNext()
                        }
                    })
            }, RemoteConfig.getRemoteConfig().getLong(RemoteConfig.HOME_INTER_LOADER_TIME))
        }

        // ========== FART INTERSTITIAL ==========
        private var clickCapFart = 0
        private var timeCapFart = true

        fun resetInterstitialFartTimeCap() {
            timeCapFart = false
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    timeCapFart = true
                },
                RemoteConfig.getRemoteConfig().getLong(RemoteConfig.FART_INTER_CAP_TIME.trim())
            )
        }

        fun showInterstitialAdFart(
            activity: Activity,
            fullScreenContentCallback: FullScreenAdListener? = null,
            fromActivity: String,
        ) {
            val from = fromActivity.lowercase()

            if (AdConstants.INTERSTITIAL_AD_LOADED.not() || internetConnection(activity).not() ||
                appOpenToInterstitialCapComplete.not() || activity.paymentSubscriptionLazy().value.isPurchased
            ) {
                fullScreenContentCallback?.gotoNext()
                return
            }

            clickCapFart++
            val capTypeFart = RemoteConfig.getString(RemoteConfig.FART_INTER_CAP_TYPE).trim()

            if (capTypeFart == "time".trim() && timeCapFart.not()) {
                Log.d("capping_fart", "Cap type: time | blocked due to timer not complete")
                fullScreenContentCallback?.gotoNext()
                return
            }

            if (capTypeFart == "clicks".trim() && clickCapFart < RemoteConfig.getRemoteConfig()
                    .getLong(RemoteConfig.FART_INTER_CAP_CLICKS)
            ) {
                Log.d("capping_fart", "Cap type: clicks | click count not reached yet")
                fullScreenContentCallback?.gotoNext()
                return
            }

            AdConstants.SHOWING_INTER_AD = true
            showProgress(activity)

            Handler(Looper.getMainLooper()).postDelayed({
                hideProgress()
                InterstitialFartActivity.showInterstitialAdFart(
                    activity,
                    object : FullScreenAdListener() {
                        override fun onAdDismissedFullScreenContent() {
                            resetInterstitialFartTimeCap()
                            analytics.logEvent("${from}_interstitial_fart_dismissed", null)
                        }

                        override fun onAdFailedToShowFullScreenContent(var1: AdError) {
                            val adId =
                                RemoteConfig.getString(RemoteConfig.FART_INTERSTITIAL_AD_ID).trim()
                            InterstitialFartActivity.loadInterstitialAdFart(
                                activity.applicationContext,
                                adId
                            )
                            analytics.logEvent("${from}_interstitial_fart_fail_show", null)
                        }

                        override fun onAdShowedFullScreenContent() {
                            timeCapFart = false
                            clickCapFart = 0
                            val adId =
                                RemoteConfig.getString(RemoteConfig.FART_INTERSTITIAL_AD_ID).trim()
                            InterstitialFartActivity.loadInterstitialAdFart(
                                activity.applicationContext,
                                if (AdConstants.TEST_ADS) activity.getString(R.string.Interstitial) else adId
                            )
                            analytics.logEvent("${from}_interstitial_fart_showed", null)
                        }

                        override fun onAdClicked() {
                            super.onAdClicked()
                            analytics.logEvent("${from}_interstitial_fart_clicked", null)
                        }

                        override fun onAdImpression() {
                            super.onAdImpression()
                            analytics.logEvent("${from}_interstitial_fart_impression", null)
                        }

                        override fun gotoNext() {
                            fullScreenContentCallback?.gotoNext()
                        }
                    })
            }, RemoteConfig.getRemoteConfig().getLong(RemoteConfig.FART_INTER_LOADER_TIME))
        }

        // ========== HALLOWEEN INTERSTITIAL ==========
        private var clickCapHalloween = 0
        private var timeCapHalloween = true

        fun resetInterstitialHalloweenTimeCap() {
            timeCapHalloween = false
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    timeCapHalloween = true
                },
                RemoteConfig.getRemoteConfig().getLong(RemoteConfig.HALLOWEEN_INTER_CAP_TIME.trim())
            )
        }

        fun showInterstitialAdHalloween(
            activity: Activity,
            fullScreenContentCallback: FullScreenAdListener? = null,
            fromActivity: String,
        ) {
            val from = fromActivity.lowercase()

            if (AdConstants.INTERSTITIAL_AD_LOADED.not() || internetConnection(activity).not() ||
                appOpenToInterstitialCapComplete.not() || activity.paymentSubscriptionLazy().value.isPurchased
            ) {
                fullScreenContentCallback?.gotoNext()
                return
            }

            clickCapHalloween++
            val capTypeHalloween =
                RemoteConfig.getString(RemoteConfig.HALLOWEEN_INTER_CAP_TYPE).trim()

            if (capTypeHalloween == "time".trim() && timeCapHalloween.not()) {
                Log.d("capping_halloween", "Cap type: time | blocked due to timer not complete")
                fullScreenContentCallback?.gotoNext()
                return
            }

            if (capTypeHalloween == "clicks".trim() && clickCapHalloween < RemoteConfig.getRemoteConfig()
                    .getLong(RemoteConfig.HALLOWEEN_INTER_CAP_CLICKS)
            ) {
                Log.d("capping_halloween", "Cap type: clicks | click count not reached yet")
                fullScreenContentCallback?.gotoNext()
                return
            }

            AdConstants.SHOWING_INTER_AD = true
            showProgress(activity)

            Handler(Looper.getMainLooper()).postDelayed({
                hideProgress()
                InterstitialHalloweenActivity.showInterstitialAdHalloween(
                    activity,
                    object : FullScreenAdListener() {
                        override fun onAdDismissedFullScreenContent() {
                            resetInterstitialHalloweenTimeCap()
                            analytics.logEvent("${from}_interstitial_halloween_dismissed", null)
                        }

                        override fun onAdFailedToShowFullScreenContent(var1: AdError) {
                            val adId =
                                RemoteConfig.getString(RemoteConfig.HALLOWEEN_INTERSTITIAL_AD_ID)
                                    .trim()
                            InterstitialHalloweenActivity.loadInterstitialAdHalloween(
                                activity.applicationContext,
                                adId
                            )
                            analytics.logEvent("${from}_interstitial_halloween_fail_show", null)
                        }

                        override fun onAdShowedFullScreenContent() {
                            timeCapHalloween = false
                            clickCapHalloween = 0
                            val adId =
                                RemoteConfig.getString(RemoteConfig.HALLOWEEN_INTERSTITIAL_AD_ID)
                                    .trim()
                            InterstitialHalloweenActivity.loadInterstitialAdHalloween(
                                activity.applicationContext,
                                if (AdConstants.TEST_ADS) activity.getString(R.string.Interstitial) else adId
                            )
                            analytics.logEvent("${from}_interstitial_halloween_showed", null)
                        }

                        override fun onAdClicked() {
                            super.onAdClicked()
                            analytics.logEvent("${from}_interstitial_halloween_clicked", null)
                        }

                        override fun onAdImpression() {
                            super.onAdImpression()
                            analytics.logEvent("${from}_interstitial_halloween_impression", null)
                        }

                        override fun gotoNext() {
                            fullScreenContentCallback?.gotoNext()
                        }
                    })
            }, RemoteConfig.getRemoteConfig().getLong(RemoteConfig.HALLOWEEN_INTER_LOADER_TIME))
        }

        // ========== STUNT INTERSTITIAL ==========
        private var clickCapStunt = 0
        private var timeCapStunt = true

        fun resetInterstitialStuntTimeCap() {
            timeCapStunt = false
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    timeCapStunt = true
                },
                RemoteConfig.getRemoteConfig().getLong(RemoteConfig.STUNT_GUN_INTER_CAP_TIME.trim())
            )
        }

        fun showInterstitialAdStunt(
            activity: Activity,
            fullScreenContentCallback: FullScreenAdListener? = null,
            fromActivity: String,
        ) {
            val from = fromActivity.lowercase()

            if (AdConstants.INTERSTITIAL_AD_LOADED.not() || internetConnection(activity).not() ||
                appOpenToInterstitialCapComplete.not() || activity.paymentSubscriptionLazy().value.isPurchased
            ) {
                fullScreenContentCallback?.gotoNext()
                return
            }

            clickCapStunt++
            val capTypeStunt = RemoteConfig.getString(RemoteConfig.STUNT_GUN_INTER_CAP_TYPE).trim()

            if (capTypeStunt == "time".trim() && timeCapStunt.not()) {
                Log.d("capping_stunt", "Cap type: time | blocked due to timer not complete")
                fullScreenContentCallback?.gotoNext()
                return
            }

            if (capTypeStunt == "clicks".trim() && clickCapStunt < RemoteConfig.getRemoteConfig()
                    .getLong(RemoteConfig.STUNT_GUN_INTER_CAP_CLICKS)
            ) {
                Log.d("capping_stunt", "Cap type: clicks | click count not reached yet")
                fullScreenContentCallback?.gotoNext()
                return
            }

            AdConstants.SHOWING_INTER_AD = true
            showProgress(activity)

            Handler(Looper.getMainLooper()).postDelayed({
                hideProgress()
                InterstitialStuntActivity.showInterstitialAdStunt(
                    activity,
                    object : FullScreenAdListener() {
                        override fun onAdDismissedFullScreenContent() {
                            resetInterstitialStuntTimeCap()
                            analytics.logEvent("${from}_interstitial_stunt_dismissed", null)
                        }

                        override fun onAdFailedToShowFullScreenContent(var1: AdError) {
                            val adId =
                                RemoteConfig.getString(RemoteConfig.STUNT_GUN_INTERSTITIAL_AD_ID)
                                    .trim()
                            InterstitialStuntActivity.loadInterstitialAdStunt(
                                activity.applicationContext,
                                adId
                            )
                            analytics.logEvent("${from}_interstitial_stunt_fail_show", null)
                        }

                        override fun onAdShowedFullScreenContent() {
                            timeCapStunt = false
                            clickCapStunt = 0
                            val adId =
                                RemoteConfig.getString(RemoteConfig.STUNT_GUN_INTERSTITIAL_AD_ID)
                                    .trim()
                            InterstitialStuntActivity.loadInterstitialAdStunt(
                                activity.applicationContext,
                                if (AdConstants.TEST_ADS) activity.getString(R.string.Interstitial) else adId
                            )
                            analytics.logEvent("${from}_interstitial_stunt_showed", null)
                        }

                        override fun onAdClicked() {
                            super.onAdClicked()
                            analytics.logEvent("${from}_interstitial_stunt_clicked", null)
                        }

                        override fun onAdImpression() {
                            super.onAdImpression()
                            analytics.logEvent("${from}_interstitial_stunt_impression", null)
                        }

                        override fun gotoNext() {
                            fullScreenContentCallback?.gotoNext()
                        }
                    })
            }, RemoteConfig.getRemoteConfig().getLong(RemoteConfig.STUNT_GUN_INTER_LOADER_TIME))
        }

        // ========== TRIMMER INTERSTITIAL ==========
        private var clickCapTrimmer = 0
        private var timeCapTrimmer = true

        fun resetInterstitialTrimmerTimeCap() {
            timeCapTrimmer = false
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    timeCapTrimmer = true
                },
                RemoteConfig.getRemoteConfig().getLong(RemoteConfig.TRIMMER_INTER_CAP_TIME.trim())
            )
        }

        fun showInterstitialAdTrimmer(
            activity: Activity,
            fullScreenContentCallback: FullScreenAdListener? = null,
            fromActivity: String,
        ) {
            val from = fromActivity.lowercase()

            if (AdConstants.INTERSTITIAL_AD_LOADED.not() || internetConnection(activity).not() ||
                appOpenToInterstitialCapComplete.not() || activity.paymentSubscriptionLazy().value.isPurchased
            ) {
                fullScreenContentCallback?.gotoNext()
                return
            }

            clickCapTrimmer++
            val capTypeTrimmer = RemoteConfig.getString(RemoteConfig.TRIMMER_INTER_CAP_TYPE).trim()

            if (capTypeTrimmer == "time".trim() && timeCapTrimmer.not()) {
                Log.d("capping_trimmer", "Cap type: time | blocked due to timer not complete")
                fullScreenContentCallback?.gotoNext()
                return
            }

            if (capTypeTrimmer == "clicks".trim() && clickCapTrimmer < RemoteConfig.getRemoteConfig()
                    .getLong(RemoteConfig.TRIMMER_INTER_CAP_CLICKS)
            ) {
                Log.d("capping_trimmer", "Cap type: clicks | click count not reached yet")
                fullScreenContentCallback?.gotoNext()
                return
            }

            AdConstants.SHOWING_INTER_AD = true
            showProgress(activity)

            Handler(Looper.getMainLooper()).postDelayed({
                hideProgress()
                InterstitialTrimmerActivity.showInterstitialAdTrimmer(
                    activity,
                    object : FullScreenAdListener() {
                        override fun onAdDismissedFullScreenContent() {
                            resetInterstitialTrimmerTimeCap()
                            analytics.logEvent("${from}_interstitial_trimmer_dismissed", null)
                        }

                        override fun onAdFailedToShowFullScreenContent(var1: AdError) {
                            val adId =
                                RemoteConfig.getString(RemoteConfig.TRIMMER_INTERSTITIAL_AD_ID)
                                    .trim()
                            InterstitialTrimmerActivity.loadInterstitialAdTrimmer(
                                activity.applicationContext,
                                adId
                            )
                            analytics.logEvent("${from}_interstitial_trimmer_fail_show", null)
                        }

                        override fun onAdShowedFullScreenContent() {
                            timeCapTrimmer = false
                            clickCapTrimmer = 0
                            val adId =
                                RemoteConfig.getString(RemoteConfig.TRIMMER_INTERSTITIAL_AD_ID)
                                    .trim()
                            InterstitialTrimmerActivity.loadInterstitialAdTrimmer(
                                activity.applicationContext,
                                if (AdConstants.TEST_ADS) activity.getString(R.string.Interstitial) else adId
                            )
                            analytics.logEvent("${from}_interstitial_trimmer_showed", null)
                        }

                        override fun onAdClicked() {
                            super.onAdClicked()
                            analytics.logEvent("${from}_interstitial_trimmer_clicked", null)
                        }

                        override fun onAdImpression() {
                            super.onAdImpression()
                            analytics.logEvent("${from}_interstitial_trimmer_impression", null)
                        }

                        override fun gotoNext() {
                            fullScreenContentCallback?.gotoNext()
                        }
                    })
            }, RemoteConfig.getRemoteConfig().getLong(RemoteConfig.TRIMMER_INTER_LOADER_TIME))
        }

        // ========== VEHICLE INTERSTITIAL ==========
        private var clickCapVehicle = 0
        private var timeCapVehicle = true

        fun resetInterstitialVehicleTimeCap() {
            timeCapVehicle = false
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    timeCapVehicle = true
                },
                RemoteConfig.getRemoteConfig().getLong(RemoteConfig.VEHICLE_INTER_CAP_TIME.trim())
            )
        }

        fun showInterstitialAdVehicle(
            activity: Activity,
            fullScreenContentCallback: FullScreenAdListener? = null,
            fromActivity: String,
        ) {
            val from = fromActivity.lowercase()

            if (AdConstants.INTERSTITIAL_AD_LOADED.not() || internetConnection(activity).not() ||
                appOpenToInterstitialCapComplete.not() || activity.paymentSubscriptionLazy().value.isPurchased
            ) {
                fullScreenContentCallback?.gotoNext()
                return
            }

            clickCapVehicle++
            val capTypeVehicle = RemoteConfig.getString(RemoteConfig.VEHICLE_INTER_CAP_TYPE).trim()

            if (capTypeVehicle == "time".trim() && timeCapVehicle.not()) {
                Log.d("capping_vehicle", "Cap type: time | blocked due to timer not complete")
                fullScreenContentCallback?.gotoNext()
                return
            }

            if (capTypeVehicle == "clicks".trim() && clickCapVehicle < RemoteConfig.getRemoteConfig()
                    .getLong(RemoteConfig.VEHICLE_INTER_CAP_CLICKS)
            ) {
                Log.d("capping_vehicle", "Cap type: clicks | click count not reached yet")
                fullScreenContentCallback?.gotoNext()
                return
            }

            AdConstants.SHOWING_INTER_AD = true
            showProgress(activity)

            Handler(Looper.getMainLooper()).postDelayed({
                hideProgress()
                InterstitialVehicleActivity.showInterstitialAdVehicle(
                    activity,
                    object : FullScreenAdListener() {
                        override fun onAdDismissedFullScreenContent() {
                            resetInterstitialVehicleTimeCap()
                            analytics.logEvent("${from}_interstitial_vehicle_dismissed", null)
                        }

                        override fun onAdFailedToShowFullScreenContent(var1: AdError) {
                            val adId =
                                RemoteConfig.getString(RemoteConfig.VEHICLE_INTERSTITIAL_AD_ID)
                                    .trim()
                            InterstitialVehicleActivity.loadInterstitialAdVehicle(
                                activity.applicationContext,
                                adId
                            )
                            analytics.logEvent("${from}_interstitial_vehicle_fail_show", null)
                        }

                        override fun onAdShowedFullScreenContent() {
                            timeCapVehicle = false
                            clickCapVehicle = 0
                            val adId =
                                RemoteConfig.getString(RemoteConfig.VEHICLE_INTERSTITIAL_AD_ID)
                                    .trim()
                            InterstitialVehicleActivity.loadInterstitialAdVehicle(
                                activity.applicationContext,
                                if (AdConstants.TEST_ADS) activity.getString(R.string.Interstitial) else adId
                            )
                            analytics.logEvent("${from}_interstitial_vehicle_showed", null)
                        }

                        override fun onAdClicked() {
                            super.onAdClicked()
                            analytics.logEvent("${from}_interstitial_vehicle_clicked", null)
                        }

                        override fun onAdImpression() {
                            super.onAdImpression()
                            analytics.logEvent("${from}_interstitial_vehicle_impression", null)
                        }

                        override fun gotoNext() {
                            fullScreenContentCallback?.gotoNext()
                        }
                    })
            }, RemoteConfig.getRemoteConfig().getLong(RemoteConfig.VEHICLE_INTER_LOADER_TIME))
        }

        private var progressDialog: AlertDialog? = null
        private fun showProgress(
            mContext: Activity,
            msg: String = mContext.getString(R.string.loading_ad),
        ) {
            hideProgress()
            val progressBinding = ProgressDailogBinding.inflate(mContext.layoutInflater)
            progressBinding.tvLoaderMSg.text = msg
            progressDialog =
                AlertDialog.Builder(mContext).setView(progressBinding.root).setCancelable(false)
                    .show()
            progressDialog?.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        }

        @JvmStatic
        fun hideProgress() {
            if (progressDialog?.isShowing == true) {
                progressDialog?.dismiss()
            }
        }

    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleNow.wrapContext(base))
        MultiDex.install(base)
    }
}

fun Context.paymentSubscriptionLazy() = lazy {
    PaymentSubscriptionKotlin(this)
}

val analytics by lazy { Firebase.analytics }
fun internetConnection(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: Network? = connectivityManager.activeNetwork
    val networkCapabilities: NetworkCapabilities? =
        connectivityManager.getNetworkCapabilities(activeNetwork)
    return networkCapabilities?.run {
        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    } == true
}