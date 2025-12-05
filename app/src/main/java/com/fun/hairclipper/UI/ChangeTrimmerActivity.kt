package com.`fun`.hairclipper.UI

import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.view.View
import com.`fun`.hairclipper.R
import com.`fun`.hairclipper.admobHelper.AdConstants
import com.`fun`.hairclipper.admobHelper.BannerAd
import com.`fun`.hairclipper.admobHelper.FullScreenAdListener
import com.`fun`.hairclipper.admobHelper.MyApplication
import com.`fun`.hairclipper.admobHelper.RemoteConfig
import com.`fun`.hairclipper.admobHelper.internetConnection
import com.`fun`.hairclipper.databinding.ActivityChangeTrimmerBinding

class ChangeTrimmerActivity : BaseClass() {
    private val binding by lazy { ActivityChangeTrimmerBinding.inflate(layoutInflater) }
    private lateinit var audioManager: AudioManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        volumeControlStream = AudioManager.STREAM_MUSIC
        setVolumeMax()
        loadCollapsibleBanner()

        binding.homeBtn.setOnClickListener { handleBackPressed() }
        binding.machine1.setOnClickListener {
            startActivity(Intent(this, Machine1::class.java))
        }
        binding.machine2.setOnClickListener { showInterstitialAndNavigate(Machine2::class.java) }
        binding.machine3.setOnClickListener { showInterstitialAndNavigate(Machine3::class.java) }
        binding.machine4.setOnClickListener { showInterstitialAndNavigate(Machine4::class.java) }
        binding.machine5.setOnClickListener { showInterstitialAndNavigate(Machine5::class.java) }
        binding.machine6.setOnClickListener { showInterstitialAndNavigate(Machine6::class.java) }
        binding.machine7.setOnClickListener { showInterstitialAndNavigate(Machine7::class.java) }
        binding.machine8.setOnClickListener { showInterstitialAndNavigate(Machine8::class.java) }
        binding.machine9.setOnClickListener { showInterstitialAndNavigate(Machine9::class.java) }
        binding.machine10.setOnClickListener { showInterstitialAndNavigate(Machine10::class.java) }
        binding.machine11.setOnClickListener { showInterstitialAndNavigate(Machine11::class.java) }
        binding.machine12.setOnClickListener { showInterstitialAndNavigate(Machine12::class.java) }
        binding.machine13.setOnClickListener { showInterstitialAndNavigate(Machine13::class.java) }
    }

    private fun showInterstitialAndNavigate(activity: Class<*>) {
        MyApplication.showInterstitialAdTrimmer(this, object : FullScreenAdListener() {
            override fun gotoNext() {
                super.gotoNext()
                startActivity(Intent(this@ChangeTrimmerActivity, activity))
            }
        }, "btn_back_change_trimmer")
    }

    private fun loadCollapsibleBanner() {
        val adId = if (AdConstants.TEST_ADS) {
            getString(R.string.BannerAd)
        } else {
            RemoteConfig.getString(RemoteConfig.CHANGE_TRIMMER_BANNER_AD_ID)
        }
        val makeCollapsible =
            RemoteConfig.getBoolean(RemoteConfig.CHANGE_TRIMMER_BANNER_MAKE_COLLAPSIBLE)
        val enable = RemoteConfig.getBoolean(RemoteConfig.ENABLE_CHANGE_TRIMMER_BANNER_AD)

        if (paymentSubscription.isPurchased.not() && internetConnection(this) && enable) {
            BannerAd.loadTop(binding.adaptivemain, adId.trim(), makeCollapsible)
        } else {
            binding.adaptivemain.visibility = View.GONE
        }
    }

    override fun handleBackPressed() {
        MyApplication.showInterstitialAdTrimmer(this, object : FullScreenAdListener() {
            override fun gotoNext() {
                super.gotoNext()
                finish()
            }
        }, "btn_back_change_trimmer")
    }

    private fun setVolumeMax() {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 1, 0)
    }
}