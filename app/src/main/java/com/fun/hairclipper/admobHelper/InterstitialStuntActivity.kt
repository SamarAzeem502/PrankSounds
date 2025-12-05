package com.`fun`.hairclipper.admobHelper

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

object InterstitialStuntActivity {
    private var interstitialAdRequesting = false
    private var interstitialAd: InterstitialAd? = null

    fun loadInterstitialAdStunt(
        mContext: Context,
        adUnitId: String,
        interstitialAdLoadCallback: InterstitialAdLoadCallback? = null,
    ) {
        if (interstitialAdRequesting ||
            internetConnection(mContext).not() ||
            interstitialAd != null
            || mContext.paymentSubscriptionLazy().value.isPurchased ||
            !RemoteConfig.getBoolean(RemoteConfig.ENABLE_STUNT_GUN_INTERSTITIAL_AD)

        ) {
            Log.d("loading...", "InterstitialStuntActivity: $interstitialAdRequesting")
            Log.d("loading...", "InterstitialStuntActivity: ${internetConnection(mContext).not()}")
            Log.d("loading...", "InterstitialStuntActivity: ${interstitialAd != null}")
            Log.d(
                "loading...",
                "InterstitialStuntActivity: ${!RemoteConfig.getBoolean(RemoteConfig.ENABLE_STUNT_GUN_INTERSTITIAL_AD)}"
            )
            return
        }
        val adRequests = AdRequest.Builder().build()
        interstitialAdRequesting = true
        AdConstants.INTERSTITIAL_AD_LOADED = false
        InterstitialAd.load(
            mContext, adUnitId, adRequests,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    AdConstants.INTERSTITIAL_AD_LOADED = false
                    interstitialAd = null
                    interstitialAdRequesting = false
                    interstitialAdLoadCallback?.onAdFailedToLoad(p0)
                    Log.d(
                        "loading...",
                        "InterstitialStuntActivity_failed" + p0.responseInfo?.responseId
                    )
                }

                override fun onAdLoaded(p0: InterstitialAd) {
                    interstitialAd = p0
                    AdConstants.INTERSTITIAL_AD_LOADED = true
                    interstitialAdRequesting = false
                    interstitialAdLoadCallback?.onAdLoaded(p0)
                    Log.d("loading...", "InterstitialStuntActivity_loaded")
                }
            })
    }

    fun showInterstitialAdStunt(activity: Activity, callback: FullScreenAdListener? = null) {

        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                callback?.onAdClicked()
            }

            override fun onAdDismissedFullScreenContent() {
                AdConstants.SHOWING_INTER_AD = false
                callback?.onAdDismissedFullScreenContent()
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                AdConstants.SHOWING_INTER_AD = false
                callback?.onAdFailedToShowFullScreenContent(p0)
            }

            override fun onAdImpression() {
                callback?.onAdImpression()
            }

            override fun onAdShowedFullScreenContent() {
                AdConstants.SHOWING_INTER_AD = true
                interstitialAd = null
                callback?.onAdShowedFullScreenContent()
            }
        }

        callback?.gotoNext()
        if (AdConstants.SHOWING_APP_OPEN_AD.not()) {
            interstitialAd?.show(activity)
        }
    }
}
