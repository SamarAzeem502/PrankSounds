package com.`fun`.hairclipper.admobHelper

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat
import com.`fun`.hairclipper.R
import com.`fun`.hairclipper.tools.AdsManager
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import java.util.concurrent.Executors

import java.util.regex.Pattern

object NativeAd {

    fun load(
        adContainer: FrameLayout,
        adId: String,
        design: String,
        btnColor: String,
        btnTextColor: String,
        makeBtnRound: Boolean,
        fromActivity: String,
        returnAd: (nativeAd: NativeAd?) -> Unit
    ) {

        val from = fromActivity.lowercase()
        adContainer.visibility = View.VISIBLE
        inflateAdViewAsync(adContainer.context, getShimmerByDesign(design)) { shimmerView ->
            adContainer.removeAllViews()
            adContainer.addView(shimmerView)
        }

//        adContainer.addView(
//            LayoutInflater.from(adContainer.context)
//                .inflate(getShimmerByDesign(design), null, false)
//        )
        val builder = AdLoader.Builder(
            adContainer.context, if (AdConstants.TEST_ADS) {
                adContainer.context.getString(R.string.native_test_ad_id)
            } else {
                adId
            }
        ).forNativeAd { nativeAd: NativeAd ->
            showNativeAd(design, nativeAd, adContainer, btnColor, btnTextColor, makeBtnRound)
            returnAd.invoke(nativeAd)

        }
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_LEFT)
                    .build()
            )
        builder.withAdListener(object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                analytics.logEvent("${from}_native_loaded", null)
            }

            override fun onAdClicked() {
                super.onAdClicked()
                analytics.logEvent("${from}_native_clicked", null)
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                returnAd.invoke(null)
                super.onAdFailedToLoad(p0)
                adContainer.visibility = View.GONE
                analytics.logEvent("${from}_native_failed", null)
                Log.i(TAG, "onAdFailedToLoad: ${p0.message}")
                returnAd.invoke(null)
            }

            override fun onAdImpression() {
                super.onAdImpression()
                analytics.logEvent("${from}_native_impression", null)
            }
        })
        builder.build().loadAd(AdRequest.Builder().build())
    }

    private fun inflateAdViewAsync(context: Context, layoutRes: Int, callback: (View) -> Unit) {
        Executors.newSingleThreadExecutor().execute {
            val view = LayoutInflater.from(context).inflate(layoutRes, null)
            Handler(Looper.getMainLooper()).post {
                callback(view)
            }
        }
    }

    private fun getShimmerByDesign(design: String): Int {
        return when (design) {
            "1a" -> R.layout.native_1_placeholder
//            "1b" -> R.layout.native_1_placeholder
//            "2a" -> R.layout.native_2_placeholder
//            "2b" -> R.layout.native_2_placeholder
//            "3a" -> R.layout.native_3_placeholder
//            "3b" -> R.layout.native_3_placeholder
//            "6a" -> R.layout.native_6_placeholder
//            "6b" -> R.layout.native_6_placeholder
//            "7a" -> R.layout.native_7_placeholder
            else -> R.layout.native_1_placeholder
        }
    }

    private fun getAdLayoutByDesign(design: String): Int {
        return when (design) {
            "1a" -> R.layout.native_1a
//            "1b" -> R.layout.native_1b
//            "2a" -> R.layout.native_2a
//            "2b" -> R.layout.native_2b
//            "3a" -> R.layout.native_3a
//            "3b" -> R.layout.native_3b
//            "6a" -> R.layout.native_6a
//            "6b" -> R.layout.native_6b
//            "7a" -> R.layout.native_7a
            else -> R.layout.native_1a
        }
    }

    private fun isValidColorHash(hashCode: String): Boolean {
        val hexPattern = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")
        val matcher = hexPattern.matcher(hashCode)
        return matcher.matches()
    }

    fun showNativeAd(
        design: String, nativeAd: NativeAd, adContainer: FrameLayout,
        btnColor: String,
        btnTextColor: String,
        makeBtnRound: Boolean
    ) {
        val adView = LayoutInflater.from(adContainer.context)
            .inflate(getAdLayoutByDesign(design), null) as NativeAdView
        val btn = adView.findViewById<Button>(R.id.ad_call_to_action)
        try {
            if (design != "3(b)") {
                if (makeBtnRound) {
                    btn.background = ResourcesCompat.getDrawable(
                        adContainer.context.resources,
                        R.drawable.bg_btn_enabled,
                        null
                    )
                } else {
                    btn.background = ResourcesCompat.getDrawable(
                        adContainer.context.resources,
                        R.drawable.bg_native_simple,
                        null
                    )
                }
            }

            if (isValidColorHash(btnColor)) {
                btn.background.setTint(Color.parseColor(btnColor))
            }
            if (isValidColorHash(btnTextColor)) {
                btn.setTextColor(Color.parseColor(btnTextColor))
            }
        } catch (e: Exception) {
            Log.d("analog", "showNativeAd: " + e.message)
        }
        AdsManager.populateUnifiedNativeAdView(nativeAd, adView)
        adContainer.removeAllViews()
        adContainer.addView(adView)
    }

    private const val TAG = "NativeAd"
}