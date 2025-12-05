package com.`fun`.hairclipper.admobHelper

import android.os.CountDownTimer
import com.google.android.gms.ads.nativead.NativeAd

object AdConstants {
    var TEST_ADS = true
    var INTRO_NATIVE_AD: NativeAd? = null
    var SHOWING_INTER_AD = false
    var SHOWING_APP_OPEN_AD = false
    var INTERSTITIAL_AD_LOADED = false
    var rewardedToAppOpenComplete = true

    var appOpenToInterstitialCap: CountDownTimer = object : CountDownTimer(
        10000, 1000
    ) {
        override fun onTick(millisUntilFinished: Long) {}
        override fun onFinish() {
            appOpenToInterstitialCapComplete = true
        }
    }

    var interstitialToAppOpenCap: CountDownTimer = object : CountDownTimer(
        10000, 1000
    ) {
        override fun onTick(millisUntilFinished: Long) {}
        override fun onFinish() {
            interstitialToAppOpenCapComplete = true
        }
    }

    var appOpenToInterstitialCapComplete = true
    var interstitialToAppOpenCapComplete = true
}