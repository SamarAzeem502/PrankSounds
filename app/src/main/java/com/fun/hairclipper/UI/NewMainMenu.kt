package com.`fun`.hairclipper.UI

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.GravityCompat
import com.`fun`.hairclipper.R
import com.`fun`.hairclipper.admobHelper.analytics
import com.`fun`.hairclipper.admobHelper.internetConnection
import com.`fun`.hairclipper.databinding.ActivityNewMainMenuBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class NewMainMenu : BaseClass() {
    private var mInterstitialAd: InterstitialAd? = null
    private var backDialog: AlertDialog? = null
    private val binding by lazy { ActivityNewMainMenuBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        analytics.logEvent("main_menu_activity", null)
        loadInterstitial()
        loadMainNativeAd()

        binding.more.setOnClickListener {
            analytics.logEvent("btn_more", null)
            drawerClick()
        }
        binding.fartCard.setOnClickListener {
            analytics.logEvent("btn_fart", null)
            if (mInterstitialAd != null) {
                mInterstitialAd!!.show(this@NewMainMenu)
                mInterstitialAd!!.fullScreenContentCallback = object :
                    FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        mInterstitialAd = null
                        startActivity(Intent(this@NewMainMenu, FartActivity::class.java))
                    }
                }
            } else {
                startActivity(Intent(this@NewMainMenu, FartActivity::class.java))
            }
        }
        binding.halloweenCard.setOnClickListener {
            analytics.logEvent("btn_halloween", null)
            if (mInterstitialAd != null) {
                mInterstitialAd!!.show(this@NewMainMenu)
                mInterstitialAd!!.fullScreenContentCallback = object :
                    FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        mInterstitialAd = null
                        startActivity(Intent(this@NewMainMenu, HalloweenActivity::class.java))
                    }
                }
            } else {
                startActivity(Intent(this@NewMainMenu, HalloweenActivity::class.java))
            }
        }
        binding.airHornCard.setOnClickListener {
            analytics.logEvent("btn_air_horn", null)
            if (mInterstitialAd != null) {
                mInterstitialAd!!.show(this@NewMainMenu)
                mInterstitialAd!!.fullScreenContentCallback = object :
                    FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        mInterstitialAd = null
                        startActivity(Intent(this@NewMainMenu, AirHornActivity::class.java))
                    }
                }
            } else {
                startActivity(Intent(this@NewMainMenu, AirHornActivity::class.java))
            }
        }
        binding.stunGunCard.setOnClickListener {
            analytics.logEvent("btn_stun_gun", null)
            if (mInterstitialAd != null) {
                mInterstitialAd!!.show(this@NewMainMenu)
                mInterstitialAd!!.fullScreenContentCallback = object :
                    FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        mInterstitialAd = null
                        startActivity(Intent(this@NewMainMenu, StunGunActivity::class.java))
                    }
                }
            } else {
                startActivity(Intent(this@NewMainMenu, StunGunActivity::class.java))
            }
        }
        binding.trimmerMenu.setOnClickListener {
            analytics.logEvent("btn_trimmer", null)
            if (mInterstitialAd != null) {
                mInterstitialAd!!.show(this@NewMainMenu)
                mInterstitialAd!!.fullScreenContentCallback = object :
                    FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        mInterstitialAd = null
                        startActivity(Intent(this@NewMainMenu, ChangeTrimmerActivity::class.java))
                    }
                }
            } else {
                startActivity(Intent(this@NewMainMenu, ChangeTrimmerActivity::class.java))
            }
        }
        binding.includeLayout.layPrivacy.setOnClickListener {
            analytics.logEvent("btn_privacy", null)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            privacyPolicy()
        }
        binding.includeLayout.layRemoveAdsDraw.setOnClickListener {
            analytics.logEvent("btn_remove_ads", null)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, PremiumScreen::class.java))
        }
        binding.includeLayout.layShareDrawer.setOnClickListener {
            analytics.logEvent("btn_share", null)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            shareApp()
        }
        binding.includeLayout.layRateDrawer.setOnClickListener {
            analytics.logEvent("btn_rate", null)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            rateApp()
        }
        binding.includeLayout.layTheme.setOnClickListener {
            analytics.logEvent("btn_theme", null)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, ThemeSelectionActivity::class.java))
        }
        binding.includeLayout.layLanguage.setOnClickListener {
            analytics.logEvent("btn_language", null)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(
                Intent(this, LanguageSelection::class.java)
                    .putExtra("fromOption", true)
            )
        }
        binding.includeLayout.viewFree.setOnClickListener {}
        if (paymentSubscription.isPurchased) {
            binding.includeLayout.layRemoveAdsDraw.visibility = View.GONE
            binding.includeLayout.linePro.visibility = View.GONE
        }
    }


    override fun handleBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (!this@NewMainMenu.isDestroyed) {
                showBackDialog()
            }
        }
    }


    fun showBackDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater: LayoutInflater = layoutInflater
        val dialogView: View = inflater.inflate(R.layout.back_dialog, null)
        builder.setView(dialogView)
        val bannerBack = dialogView.findViewById<FrameLayout>(R.id.frameBack)
        val btnYes = dialogView.findViewById<Button>(R.id.btnYes)
        val btnNo = dialogView.findViewById<Button>(R.id.btnNo)
        btnYes.setOnClickListener {
            analytics.logEvent("exit_dialog_btn_yes", null)
            backDialog!!.dismiss()
            finishAndRemoveTask()
            finishAffinity()
        }

        btnNo.setOnClickListener {
            analytics.logEvent("exit_dialog_btn_no", null)
            if (backDialog != null && backDialog!!.isShowing) {
                backDialog!!.dismiss()
            }
        }
        if (paymentSubscription.isPurchased.not() && internetConnection(this)) {
            com.`fun`.hairclipper.admobHelper.NativeAd.load(
                bannerBack,
                getString(R.string.native_ad), "", "", "", true, "back_dialog"
            ) {}
        } else {
            bannerBack.visibility = View.GONE
        }

        backDialog = builder.create()
        backDialog!!.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        backDialog!!.show()
    }


    fun drawerClick() {
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    private fun loadInterstitial() {
        if (paymentSubscription.isPurchased.not() && internetConnection(this)) {
            val adRequest = AdRequest.Builder().build()
            InterstitialAd.load(
                this, getString(R.string.Interstitial), adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd
                        Log.i(ContentValues.TAG, "onAdLoaded")
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        // Handle the error
                        Log.d(ContentValues.TAG, loadAdError.toString())
                        mInterstitialAd = null
                    }
                })
        }
    }

    private fun loadMainNativeAd() {
        if (paymentSubscription.isPurchased.not() && internetConnection(this)) {
            com.`fun`.hairclipper.admobHelper.NativeAd.load(
                binding.frameMainNative,
                getString(R.string.native_ad),
                "", "", "", true, "main"
            ) {}
        } else {
            binding.frameMainNative.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        analytics.logEvent("main_menu_activity_destroy", null)
        super.onDestroy()
    }
}