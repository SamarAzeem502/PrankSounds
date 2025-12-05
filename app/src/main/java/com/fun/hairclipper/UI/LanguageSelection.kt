package com.`fun`.hairclipper.UI

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.edit
import com.`fun`.hairclipper.R
import com.`fun`.hairclipper.adaptor.LanguageAdapter
import com.`fun`.hairclipper.admobHelper.AdConstants
import com.`fun`.hairclipper.admobHelper.InterstitialFartActivity
import com.`fun`.hairclipper.admobHelper.InterstitialHalloweenActivity
import com.`fun`.hairclipper.admobHelper.InterstitialStuntActivity
import com.`fun`.hairclipper.admobHelper.InterstitialVehicleActivity
import com.`fun`.hairclipper.admobHelper.MyApplication
import com.`fun`.hairclipper.admobHelper.RemoteConfig
import com.`fun`.hairclipper.admobHelper.internetConnection
import com.`fun`.hairclipper.databinding.ActivityLanguageSelectionBinding
import com.`fun`.hairclipper.helpers.Constants
import com.`fun`.hairclipper.helpers.LocaleNow

class LanguageSelection : BaseClass() {
    private val binding by lazy { ActivityLanguageSelectionBinding.inflate(layoutInflater) }
    private var bundle = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.recyclerView.adapter = adapter
        binding.ivApply.setOnClickListener { applyLanguage() }
        binding.ivBack.setOnClickListener { handleBackPressed() }
        bundle = intent.getBooleanExtra("fromOption", false)
        if (bundle) {
            binding.ivBack.visibility = View.VISIBLE
            binding.viewName.visibility = View.GONE
        } else {
            binding.ivBack.visibility = View.GONE
            binding.viewName.visibility = View.VISIBLE
        }
        val selectedLangIndex = settings.getInt(Constants.KEY_SELECTED_LANG, -1)
        if (selectedLangIndex != -1) {
            val selectedLanguage = Constants.languages[selectedLangIndex].language
            binding.tvLangName.text = selectedLanguage.toString()
        } else {
            binding.tvLangName.text = getString(R.string.english_fix)
            settings.edit {
                putInt(Constants.KEY_SELECTED_LANG, 0)
            }
            LocaleNow.changeLanguage(this, "en")
        }

        languageNativeAd()

        val adID = if (AdConstants.TEST_ADS) getString(R.string.Interstitial)
        else RemoteConfig.getString(RemoteConfig.STUNT_GUN_INTERSTITIAL_AD_ID)
        if (internetConnection(this) && RemoteConfig.getBoolean(RemoteConfig.ENABLE_STUNT_GUN_INTERSTITIAL_AD)
        ) {
            InterstitialStuntActivity.loadInterstitialAdStunt(this, adID.trim(), null)
            MyApplication.resetInterstitialStuntTimeCap()
        }

        val adIDHalloween = if (AdConstants.TEST_ADS) getString(R.string.Interstitial)
        else RemoteConfig.getString(RemoteConfig.HALLOWEEN_INTERSTITIAL_AD_ID)
        if (internetConnection(this) && RemoteConfig.getBoolean(RemoteConfig.ENABLE_HALLOWEEN_INTERSTITIAL_AD)
        ) {
            InterstitialHalloweenActivity.loadInterstitialAdHalloween(this, adIDHalloween.trim(), null)
            MyApplication.resetInterstitialHalloweenTimeCap()
        }
    }

    private fun languageNativeAd() {
        val adId = if (AdConstants.TEST_ADS) {
            RemoteConfig.getString(RemoteConfig.LANGUAGE_NATIVE_AD_ID)
        } else {
            getString(R.string.native_ad)
        }
        val adType = RemoteConfig.getString(RemoteConfig.LANGUAGE_NATIVE_AD_TYPE)
        val adRound = RemoteConfig.getBoolean(RemoteConfig.LANGUAGE_NATIVE_BUTTON_CORNERS)
        val adBtnColor = RemoteConfig.getString(RemoteConfig.LANGUAGE_NATIVE_BUTTON_COLOR)
        val adBtnTextColor = RemoteConfig.getString(RemoteConfig.LANGUAGE_NATIVE_BUTTON_TEXT_COLOR)
        if (paymentSubscription.isPurchased.not() && internetConnection(this)
            && RemoteConfig.getBoolean(RemoteConfig.ENABLE_LANGUAGE_NATIVE_AD)
        ) {
            com.`fun`.hairclipper.admobHelper.NativeAd.load(
                binding.frameLangNative,
                adId.trim(), adType, adBtnColor, adBtnTextColor, adRound, "language"
            ) {}
        } else {
            binding.frameLangNative.visibility = View.GONE
        }
    }

    override fun handleBackPressed() {
        if (bundle) {
            finish()
        } else {
            return
        }
    }

    private val settings by lazy {
        getSharedPreferences(
            Constants.SETTINGS_PREF,
            MODE_PRIVATE
        )
    }

    private val adapter by lazy {
        LanguageAdapter(
            activity = this,
            recyclerItems = Constants.languages,
            selectedPos = settings.getInt(Constants.KEY_SELECTED_LANG, 0)
        )
    }

    private fun applyLanguage() {
        if (settings.getBoolean(Constants.KEY_IS_FIRST_TIME, true)) {
            settings.edit {
                putBoolean(Constants.KEY_IS_FIRST_TIME, false)
            }
            startActivity(Intent(this, NewMainMenu::class.java))
        }
        settings.edit {
            putInt(Constants.KEY_SELECTED_LANG, adapter.selectedPos)
        }
        val languageModel = Constants.languages[adapter.selectedPos]
        LocaleNow.changeLanguage(this, languageModel.langCode)
        startActivity(Intent(this, IntroActivity::class.java))
        finish()
    }
}