package com.`fun`.hairclipper.UI

import android.content.Intent
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.`fun`.hairclipper.R
import com.`fun`.hairclipper.adaptor.IntroAdapterFullNative
import com.`fun`.hairclipper.admobHelper.AdConstants
import com.`fun`.hairclipper.admobHelper.InterstitialFartActivity
import com.`fun`.hairclipper.admobHelper.InterstitialHomeActivity
import com.`fun`.hairclipper.admobHelper.InterstitialVehicleActivity
import com.`fun`.hairclipper.admobHelper.MyApplication
import com.`fun`.hairclipper.admobHelper.RemoteConfig
import com.`fun`.hairclipper.admobHelper.internetConnection
import com.`fun`.hairclipper.databinding.ActivityIntroBinding

class IntroActivity : BaseClass() {
    val binding by lazy { ActivityIntroBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        binding.screenViewpager.adapter = IntroAdapterFullNative(this)
        binding.screenViewpager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (AdConstants.INTRO_NATIVE_AD != null) {
                    when (position) {
                        0 -> {
                            AdConstants.SHOWING_INTER_AD = false
                        }

                        1 -> {
                            AdConstants.SHOWING_INTER_AD = true
                        }

                        2 -> {
                            AdConstants.SHOWING_INTER_AD = false
                        }
                    }
                } else {
                    AdConstants.SHOWING_INTER_AD = false
                }
            }
        })

        val adID = if (AdConstants.TEST_ADS) getString(R.string.Interstitial)
        else RemoteConfig.getString(RemoteConfig.FART_INTERSTITIAL_AD_ID)
        if (internetConnection(this) && RemoteConfig.getBoolean(RemoteConfig.ENABLE_FART_INTERSTITIAL_AD)
        ) {
            InterstitialFartActivity.loadInterstitialAdFart(this, adID.trim(), null)
            MyApplication.resetInterstitialFartTimeCap()
        }

        val adIDVehicle = if (AdConstants.TEST_ADS) getString(R.string.Interstitial)
        else RemoteConfig.getString(RemoteConfig.VEHICLE_INTERSTITIAL_AD_ID)
        if (internetConnection(this) && RemoteConfig.getBoolean(RemoteConfig.ENABLE_VEHICLE_INTERSTITIAL_AD)
        ) {
            InterstitialVehicleActivity.loadInterstitialAdVehicle(this, adIDVehicle.trim(), null)
            MyApplication.resetInterstitialVehicleTimeCap()
        }
    }

    fun next(position: Int) {
        if (position == 1) {
//            fullBatteryPrefs.saveFirstLaunch(true)
            startActivity(Intent(this, NewMainMenu::class.java))
            finish()
        } else {
            binding.screenViewpager.currentItem += 1
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}