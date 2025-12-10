package com.`fun`.hairclipper.UI

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Vibrator
import android.view.View
import android.widget.CompoundButton
import com.`fun`.hairclipper.R
import com.`fun`.hairclipper.admobHelper.AdConstants
import com.`fun`.hairclipper.admobHelper.BannerAd
import com.`fun`.hairclipper.admobHelper.FullScreenAdListener
import com.`fun`.hairclipper.admobHelper.MyApplication
import com.`fun`.hairclipper.admobHelper.RemoteConfig
import com.`fun`.hairclipper.admobHelper.internetConnection
import com.`fun`.hairclipper.databinding.ActivityMachine9Binding

class Machine9 : BaseClass() {
    private var vibrator: Vibrator? = null
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var binding: ActivityMachine9Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMachine9Binding.inflate(layoutInflater)
        setContentView(binding.root)
        loadBannerAd()
        binding.homeBtn.setOnClickListener { handleBackPressed() }
        mediaPlayer = MediaPlayer.create(this@Machine9, R.raw.hair_clipper9)
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        binding.toggleButton1.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                vibrator!!.vibrate(300000)
                mediaPlayer!!.start()
            } else {
                MyApplication.showInterstitialAdTrimmer(this, object : FullScreenAdListener() {
                    override fun gotoNext() {
                        super.gotoNext()
                        vibrator!!.cancel()
                        mediaPlayer!!.stop()
                        mediaPlayer!!.prepareAsync()
                    }
                }, "stop_machine_9")
            }
        }

        mediaPlayer!!.setOnCompletionListener { _: MediaPlayer? ->
            vibrator!!.cancel()
            binding.toggleButton1.isChecked = false
        }
    }

    override fun handleBackPressed() {
        MyApplication.showInterstitialAdTrimmer(this, object : FullScreenAdListener() {
            override fun gotoNext() {
                super.gotoNext()
                vibrator!!.cancel()
                mediaPlayer!!.stop()
                finish()
            }
        }, "btn_back_m9")

    }

    private fun loadBannerAd() {
        val adId = if (AdConstants.TEST_ADS) {
            getString(R.string.BannerAd)
        } else {
            RemoteConfig.getString(RemoteConfig.MACHINE9_BANNER_AD_ID)
        }
        val makeCollapsible = RemoteConfig.getBoolean(RemoteConfig.MACHINE9_BANNER_MAKE_COLLAPSIBLE)
        val enable = RemoteConfig.getBoolean(RemoteConfig.ENABLE_MACHINE9_BANNER_AD)

        if (paymentSubscription.isPurchased.not() && internetConnection(this) && enable) {
            BannerAd.load(binding.adaptive, adId.trim(), makeCollapsible)
        } else {
            binding.adaptive.visibility = View.GONE
        }
    }
}