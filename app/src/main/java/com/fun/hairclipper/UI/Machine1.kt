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
import com.`fun`.hairclipper.databinding.ActivityMachine1Binding

class Machine1 : BaseClass() {
    private val binding by lazy { ActivityMachine1Binding.inflate(layoutInflater) }
    var vibrator: Vibrator? = null
    var mediaPlayer: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        loadBannerAD()
        binding.homeBtn.setOnClickListener { handleBackPressed() }
        mediaPlayer = MediaPlayer.create(this@Machine1, R.raw.hair_clipper1)
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
                }, "stop_machine_1")
            }
        }

        mediaPlayer!!.setOnCompletionListener { _: MediaPlayer? ->
            vibrator!!.cancel()
            binding.toggleButton1.isChecked = false
        }
    }


    private fun loadBannerAD() {
        val adId = if (AdConstants.TEST_ADS) {
            getString(R.string.BannerAd)
        } else {
            RemoteConfig.getString(RemoteConfig.MACHINE1_BANNER_AD_ID)
        }
        val makeCollapsible = RemoteConfig.getBoolean(RemoteConfig.MACHINE1_BANNER_MAKE_COLLAPSIBLE)
        val enable = RemoteConfig.getBoolean(RemoteConfig.ENABLE_MACHINE1_BANNER_AD)

        if (paymentSubscription.isPurchased.not() && internetConnection(this) && enable) {
            BannerAd.load(binding.adaptive, adId.trim(), makeCollapsible)
        } else {
            binding.adaptive.visibility = View.GONE
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
        }, "btn_back_m1")

    }
}