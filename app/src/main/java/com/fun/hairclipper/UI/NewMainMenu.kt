package com.`fun`.hairclipper.UI

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.GravityCompat
import com.`fun`.hairclipper.R
import com.`fun`.hairclipper.admobHelper.AdConstants
import com.`fun`.hairclipper.admobHelper.FullScreenAdListener
import com.`fun`.hairclipper.admobHelper.InterstitialTrimmerActivity
import com.`fun`.hairclipper.admobHelper.MyApplication
import com.`fun`.hairclipper.admobHelper.RemoteConfig
import com.`fun`.hairclipper.admobHelper.analytics
import com.`fun`.hairclipper.admobHelper.internetConnection
import com.`fun`.hairclipper.databinding.ActivityNewMainMenuBinding

class NewMainMenu : BaseClass() {
    private var backDialog: AlertDialog? = null
    private val binding by lazy { ActivityNewMainMenuBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        analytics.logEvent("main_menu_activity", null)
        loadMainNativeAd()
        val adID = if (AdConstants.TEST_ADS) getString(R.string.Interstitial)
        else RemoteConfig.getString(RemoteConfig.TRIMMER_INTERSTITIAL_AD_ID)
        if (internetConnection(this) && RemoteConfig.getBoolean(RemoteConfig.ENABLE_TRIMMER_INTERSTITIAL_AD)
        ) {
            InterstitialTrimmerActivity.loadInterstitialAdTrimmer(this, adID.trim(), null)
            MyApplication.resetInterstitialTrimmerTimeCap()
        }
        binding.more.setOnClickListener {
            analytics.logEvent("btn_more", null)
            drawerClick()
        }
        binding.fartCard.setOnClickListener {
            analytics.logEvent("btn_fart", null)
            showInterstitialAndNavigate(FartActivity::class.java, "btn_fart")
        }
        binding.halloweenCard.setOnClickListener {
            analytics.logEvent("btn_halloween", null)
            showInterstitialAndNavigate(HalloweenActivity::class.java, "btn_halloween")
        }
        binding.airHornCard.setOnClickListener {
            analytics.logEvent("btn_air_horn", null)
            showInterstitialAndNavigate(AirHornActivity::class.java, "btn_air_horn")
        }
        binding.stunGunCard.setOnClickListener {
            analytics.logEvent("btn_stun_gun", null)
            showInterstitialAndNavigate(StunGunActivity::class.java, "btn_stun_gun")
        }
        binding.trimmerMenu.setOnClickListener {
            analytics.logEvent("btn_trimmer", null)
            showInterstitialAndNavigate(ChangeTrimmerActivity::class.java, "btn_trimmer")
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

    private fun showInterstitialAndNavigate(activity: Class<*>, fromActivity: String) {
        MyApplication.showInterstitialAdHome(this, object : FullScreenAdListener() {
            override fun gotoNext() {
                super.gotoNext()
                startActivity(Intent(this@NewMainMenu, activity))
            }
        }, fromActivity)
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
        val adId = if (AdConstants.TEST_ADS) {
            RemoteConfig.getString(RemoteConfig.EXIT_NATIVE_AD_ID)
        } else {
            getString(R.string.native_ad)
        }
        val adType = RemoteConfig.getString(RemoteConfig.EXIT_NATIVE_AD_TYPE)
        val adRound = RemoteConfig.getBoolean(RemoteConfig.EXIT_NATIVE_BUTTON_ROUND)
        val adBtnColor = RemoteConfig.getString(RemoteConfig.EXIT_NATIVE_BUTTON_COLOR)
        val adBtnTextColor = RemoteConfig.getString(RemoteConfig.EXIT_NATIVE_BUTTON_TEXT_COLOR)
        if (paymentSubscription.isPurchased.not() && internetConnection(this)
            && RemoteConfig.getBoolean(RemoteConfig.ENABLE_EXIT_NATIVE_AD)
        ) {
            com.`fun`.hairclipper.admobHelper.NativeAd.load(
                bannerBack,
                adId, adType, adBtnColor, adBtnTextColor, adRound, "back_dialog"
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

    private fun loadMainNativeAd() {
        val adId = if (AdConstants.TEST_ADS) {
            RemoteConfig.getString(RemoteConfig.MAIN_NATIVE_AD_ID)
        } else {
            getString(R.string.native_ad)
        }
        val adType = RemoteConfig.getString(RemoteConfig.MAIN_NATIVE_AD_TYPE)
        val adRound = RemoteConfig.getBoolean(RemoteConfig.MAIN_NATIVE_BUTTON_CORNERS)
        val adBtnColor = RemoteConfig.getString(RemoteConfig.MAIN_NATIVE_BUTTON_COLOR)
        val adBtnTextColor = RemoteConfig.getString(RemoteConfig.MAIN_NATIVE_BUTTON_TEXT_COLOR)
        if (paymentSubscription.isPurchased.not() && internetConnection(this)
            && RemoteConfig.getBoolean(RemoteConfig.ENABLE_MAIN_NATIVE_AD)
        ) {
            com.`fun`.hairclipper.admobHelper.NativeAd.load(
                binding.frameMainNative,
                adId, adType, adBtnColor, adBtnTextColor, adRound, "main_menu"
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