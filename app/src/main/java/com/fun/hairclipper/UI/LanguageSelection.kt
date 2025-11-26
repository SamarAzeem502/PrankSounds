package com.`fun`.hairclipper.UI

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.edit
import com.`fun`.hairclipper.helpers.Constants
import com.`fun`.hairclipper.R
import com.`fun`.hairclipper.adaptor.LanguageAdapter
import com.`fun`.hairclipper.databinding.ActivityLanguageSelectionBinding
import com.`fun`.hairclipper.helpers.AdsManager
import com.`fun`.hairclipper.helpers.LocaleNow
import com.`fun`.hairclipper.admobHelper.internetConnection
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

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
    }

    private fun languageNativeAd() {
        if (paymentSubscription.isPurchased.not() && internetConnection(this)) {
            com.`fun`.hairclipper.admobHelper.NativeAd.load(
                binding.frameLangNative,
                getString(R.string.native_ad),
                "","","",
                false,
                ""
            ){}
        }else{
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