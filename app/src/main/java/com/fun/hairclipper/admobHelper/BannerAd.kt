package com.`fun`.hairclipper.admobHelper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.`fun`.hairclipper.R
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

object BannerAd {

    @SuppressLint("MissingPermission")
    fun load(adContainer: ViewGroup, adId: String, makeCollapsable: Boolean = true) =
        adContainer.post {
            if (internetConnection(adContainer.context).not()) {
                return@post
            }
            val placeholder = LayoutInflater.from(adContainer.context)
                .inflate(R.layout.banner_ad_placeholder, null, false)
            adContainer.visibility = View.VISIBLE
            adContainer.removeAllViews()
            adContainer.addView(placeholder)
            val adView = AdView(adContainer.context)
            adView.adUnitId = adId
            adView.setAdSize(getAdaptiveAdSize(adView))
            adView.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    adContainer.removeAllViews()
                    adContainer.addView(adView)
                    val mContext: Activity = adContainer.context as Activity
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    Log.e("bannerad", "onAdFailedToLoad: ", Exception(p0.message))
                    adContainer.removeAllViews()
                }
            }
            val adRequest = AdRequest.Builder()
            if (makeCollapsable) {
                adRequest.addNetworkExtrasBundle(AdMobAdapter::class.java, Bundle().apply {
                    putString("collapsible", "bottom")
                })
            }
            adView.loadAd(adRequest.build())
        }

    fun loadMedium(adContainer: ViewGroup, adId: String) =
        adContainer.post {
            if (internetConnection(adContainer.context).not()) {
                return@post
            }
            val placeholder = LayoutInflater.from(adContainer.context)
                .inflate(R.layout.banner_medium_placeholder, null, false)
            adContainer.visibility = View.VISIBLE
            adContainer.removeAllViews()
            adContainer.addView(placeholder)
            val adView = AdView(adContainer.context)
            val tempSize= getAdaptiveAdSize(adContainer)
            val adSize = AdSize.getInlineAdaptiveBannerAdSize(tempSize.width,tempSize.width)
            adView.adUnitId = adId
            adView.setAdSize(adSize)
            adView.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    adContainer.removeAllViews()
                    adContainer.addView(adView)
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    Log.e("bannerad", "onAdFailedToLoad: ", Exception(p0.message))
                    adContainer.removeAllViews()
                }
            }
            val adRequest = AdRequest.Builder()
            adView.loadAd(adRequest.build())
        }

    private fun getAdaptiveAdSize(adContainer: ViewGroup): AdSize {
        val display = Resources.getSystem().displayMetrics
        val adWidth = adContainer.width
        val adSize = display.widthPixels / display.density
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
            adContainer.context,
            adSize.toInt()
        )
    }

}