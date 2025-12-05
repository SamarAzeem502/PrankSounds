package com.`fun`.hairclipper.admobHelper

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.`fun`.hairclipper.R
import com.`fun`.hairclipper.UI.PremiumScreen
import com.`fun`.hairclipper.UI.SplashScreen
import com.`fun`.hairclipper.admobHelper.AdConstants.SHOWING_INTER_AD
import com.`fun`.hairclipper.admobHelper.AdConstants.appOpenToInterstitialCap
import com.`fun`.hairclipper.admobHelper.AdConstants.appOpenToInterstitialCapComplete
import com.`fun`.hairclipper.admobHelper.AdConstants.interstitialToAppOpenCapComplete
import com.`fun`.hairclipper.admobHelper.AdConstants.rewardedToAppOpenComplete
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import java.util.Date

class AppOpenManager(private val myApplication: MyApplication) : ActivityLifecycleCallbacks,
    DefaultLifecycleObserver {
    private var loadTime: Long = 0
    private var currentActivity: Activity? = null
    private var isAdLoading = false

    //    private lateinit var paymentSubscription: PaymentSubscription
    private var loadCallback: AppOpenAdLoadCallback? = null

    init {
        try {
            Log.d(LOG_TAG, ": AppOpenManager $myApplication ")
            myApplication.registerActivityLifecycleCallbacks(this)
            ProcessLifecycleOwner.get().lifecycle.addObserver(this)
//            paymentSubscription = myApplication.paymentSubscription

            Log.d(LOG_TAG, ":$myApplication ")
        } catch (e: Exception) {
            Log.e(LOG_TAG, ": ", e)
        }
    }


    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        if (currentActivity !is SplashScreen && currentActivity !is PremiumScreen) {
            showAdIfAvailable()
        } else {
            fetchAd()
        }
        Log.d(LOG_TAG, "onStart")
    }


    fun fetchAd() {
        try {// Have unused ad, no need to fetch another.

            if (isAdAvailable() || isAdLoading || myApplication.paymentSubscription.isPurchased
                || !internetConnection(myApplication)
            ) {
                return
            }
            loadCallback = object : AppOpenAdLoadCallback() {
                override fun onAdLoaded(appOpenAd: AppOpenAd) {
                    Log.d(LOG_TAG, "onAdLoaded: ")
                    analytics.logEvent("app_open_loaded", null)
                    isAdLoading = false
                    Companion.appOpenAd = appOpenAd
                    loadTime = Date().time
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    Log.d(LOG_TAG, "onAdFailedToLoad: ")
                    analytics.logEvent(
                        "app_open_fail_load", null
                    )
                    isAdLoading = false
                }
            }
            val request = AdRequest.Builder().build()
            val s: String = if (AdConstants.TEST_ADS) {
                myApplication.getString(R.string.app_open)
            } else {
                myApplication.getString(R.string.app_open_real)
            }
            isAdLoading = true
            AppOpenAd.load(myApplication, s.trim(), request, loadCallback as AppOpenAdLoadCallback)
            /* RemoteConfig.getRemoteConfig().fetchAndActivate().addOnCompleteListener(task -> {
                Log.d(LOG_TAG, "fetchAd: task: " + task.isSuccessful());
                if (RemoteConfig.getBoolean(RemoteConfig.ENABLE_APP_OPEN_AD)) {
                    AppOpenAd.load(myApplication, RemoteConfig.getString(RemoteConfig.APP_OPEN_AD), request, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
                }
            });*/
        } catch (e: Exception) {
            Log.e(LOG_TAG, "fetchAd: ", e)
        }
    }

    private fun showAdIfAvailable() {
        // Only show ad if there is not already an app open ad currently showing
        // and an ad is available.
        if (!AdConstants.SHOWING_APP_OPEN_AD
            && isAdAvailable()
            && rewardedToAppOpenComplete
            && appOpenToInterstitialCapComplete
            && interstitialToAppOpenCapComplete
            && !SHOWING_INTER_AD
        ) {
            Log.d(LOG_TAG, "Will show ad.")
            appOpenAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    AdConstants.SHOWING_APP_OPEN_AD = false
                    appOpenToInterstitialCap.start()
                    analytics.logEvent(
                        "appOpenAd_dismiss",
                        null
                    )
                    Log.d(LOG_TAG, "onAdDismissedFullScreenContent: ")
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.d(LOG_TAG, "onAdFailedToShowFullScreenContent: ")
                    analytics.logEvent("appOpenAd_failed_to_show", null)
                    AdConstants.SHOWING_APP_OPEN_AD = false
                    appOpenAd = null
                    fetchAd()
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d(LOG_TAG, "onAdShowedFullScreenContent: ")
                    analytics.logEvent("appOpenAd_showed", null)
                    appOpenToInterstitialCapComplete = false
                    AdConstants.SHOWING_APP_OPEN_AD = true
                    appOpenAd = null
                    fetchAd()
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    analytics.logEvent("appOpenAd_clicked", null)
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    analytics.logEvent("appOpenAd_impression", null)
                }
            }
            appOpenAd!!.show(currentActivity!!)
        } else {
            Log.d(LOG_TAG, "Can not show ad.")
            fetchAd()
        }
    }

    private fun isAdAvailable(): Boolean {
        return appOpenAd != null && wasLoadTimeLessThan4HoursAgo()
    }

    private fun wasLoadTimeLessThan4HoursAgo(): Boolean {
        val dateDifference = Date().time - this.loadTime
        val numMilliSecondsPerHour = 3600000
        return dateDifference < numMilliSecondsPerHour * 4
    }


    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {
        Log.d("test", "onActivityDestroyed: ${activity.localClassName} ")
        currentActivity = null
    }

    companion object {
        var appOpenAd: AppOpenAd? = null
        private const val LOG_TAG = "AppOpenManager"
    }
}