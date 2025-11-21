package com.`fun`.hairclipper.UI

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.graphics.drawable.toDrawable
import com.`fun`.hairclipper.R
import com.`fun`.hairclipper.admobHelper.AdConstants
import com.`fun`.hairclipper.databinding.SplashscreenBinding
import com.`fun`.hairclipper.tools.Tool
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MediaAspectRatio
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions

@SuppressLint("CustomSplashScreen")
class SplashScreen : BaseClass() {
    private var dialogForAd: AlertDialog? = null
    private val handler = Handler(Looper.getMainLooper())
    private val handlerProgress = Handler(Looper.getMainLooper())
    private var adLoaded = false
    private var appInitialized = false
    private val binding by lazy { SplashscreenBinding.inflate(layoutInflater) }
    private var interstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(binding.root)

        if (!paymentSubscription.isPurchased && Tool.isNetworkAvailable(this)) {
            prepareToLoadAds()
        } else {
            prepareToLoadAds()
            binding.buttonPro.visibility = View.GONE
        }
        binding.buttonStart.setOnClickListener {
            interstitialAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdShowedFullScreenContent() {
                    interstitialAd = null
                }

            }
            prepareGotoNext()
        }
        binding.buttonPro.setOnClickListener {
            startActivity(
                Intent(this, PremiumScreen::class.java)
            )
        }
    }

    override fun onDestroy() {
        hideAdDialog()
        super.onDestroy()
    }

    private fun prepareGotoNext() {
        handler.removeCallbacksAndMessages(null)
        handlerProgress.removeCallbacksAndMessages(null)
        if (interstitialAd != null) {
            showAdDialog()
            Handler(mainLooper).postDelayed({
                hideAdDialog()
                prepareNext()
            }, 1500)
        } else {
            prepareNext()
        }
    }

    private fun prepareNext() {
        startActivity(Intent(this, LanguageSelection::class.java))
        if (interstitialAd != null) {
            interstitialAd!!.show(this)
        }
        finish()
    }

    private fun prepareToLoadAds() {
        if (appInitialized) return
        appInitialized = true
        val allOkay = !paymentSubscription.isPurchased && Tool.isNetworkAvailable(this)
        val time = if (allOkay) 12000L else 6000L
        if (allOkay) {
            loadSplashAd()
        }
        binding.pbSplash.setMax(time.toInt())
        handlerProgress.post(object : Runnable {
            override fun run() {
                val progress = if (adLoaded) time.toInt() else binding.pbSplash.progress + 40
                binding.pbSplash.progress = progress
                if (progress >= time && !adLoaded) {
                    prepareGotoNext()
                } else {
                    handlerProgress.postDelayed(this, 40)
                }
            }
        })
    }

    private fun loadSplashAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            this,
            getString(R.string.SplashInterstitial),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    handler.postDelayed(adFailedToLoad, 4000)
                }

                override fun onAdLoaded(interstitial: InterstitialAd) {
                    interstitialAd = interstitial
                    adLoaded = true
                    binding.pbSplash.progress = binding.pbSplash.max
                    binding.buttonStart.visibility = View.VISIBLE

                    //                binding.pbSplash.setVisibility(View.INVISIBLE);
                }
            })
    }

    private fun showAdDialog() {
        val builder = AlertDialog.Builder(this)
        val view: View? = LayoutInflater.from(this).inflate(R.layout.progress_dailog, null)
        builder.setView(view)
        dialogForAd = builder.create()
        if (dialogForAd!!.window != null) {
            dialogForAd!!.window!!.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        }
        dialogForAd!!.show()
        dialogForAd!!.setCancelable(false)
    }

    private fun hideAdDialog() {
        if (dialogForAd != null && dialogForAd!!.isShowing) {
            dialogForAd!!.dismiss()
        }
    }

    private val adFailedToLoad = Runnable {
        adLoaded = true
        prepareGotoNext()
    }

    private fun loadIntroNativeAd() {
        if (AdConstants.INTRO_NATIVE_AD == null) {
            val videoOptions =
                VideoOptions.Builder().setStartMuted(false).setCustomControlsRequested(true).build()

            val builder = AdLoader.Builder(
                this, ""
            )
                .forNativeAd { nativeAd: NativeAd ->
                    AdConstants.INTRO_NATIVE_AD = nativeAd
                }

                .withNativeAdOptions(
                    NativeAdOptions.Builder()
                        .setMediaAspectRatio(MediaAspectRatio.ANY)
                        .setVideoOptions(videoOptions)
                        .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT)
                        .build()
                )
            builder.withAdListener(object : AdListener() {

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    Log.d(TAG, "onAdFailedToLoad: ${p0.message}")
//                    Toast.makeText(this@SplashScreen, "ad failed", Toast.LENGTH_SHORT).show()
//                    AnalyticsHandler(this@SplashActivity).logEvent("full_native_fail_load", null)
                }

            })
            builder.build().loadAd(AdRequest.Builder().build())
        }
    }

    private val TAG = "SplashScreen"
}