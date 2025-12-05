package com.`fun`.hairclipper.UI

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Vibrator
import android.view.View
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ToggleButton
import com.`fun`.hairclipper.R
import com.`fun`.hairclipper.admobHelper.AdConstants
import com.`fun`.hairclipper.admobHelper.BannerAd
import com.`fun`.hairclipper.admobHelper.FullScreenAdListener
import com.`fun`.hairclipper.admobHelper.MyApplication
import com.`fun`.hairclipper.admobHelper.RemoteConfig
import com.`fun`.hairclipper.admobHelper.internetConnection
import com.`fun`.hairclipper.databinding.ActivityMachine2Binding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

class Machine2 : BaseClass() {
    var vibrator: Vibrator? = null
    var mediaPlayer: MediaPlayer? = null
    private val binding by lazy { ActivityMachine2Binding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.homeBtn.setOnClickListener { handleBackPressed() }
        loadBannerAd()
        mediaPlayer = MediaPlayer.create(this@Machine2, R.raw.hair_clipper2)
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        binding.toggleButton1.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                vibrator!!.vibrate(300000)
                mediaPlayer!!.start()
            } else {
                MyApplication.showInterstitialAdTrimmer(this,object : FullScreenAdListener(){
                    override fun gotoNext() {
                        super.gotoNext()
                        vibrator!!.cancel()
                        mediaPlayer!!.stop()
                        mediaPlayer!!.prepareAsync()
                    }
                },"stop_machine_2")

            }
        }

        mediaPlayer!!.setOnCompletionListener { _: MediaPlayer? ->
            vibrator!!.cancel()
            binding.toggleButton1.isChecked = false
        }
    }

    override fun handleBackPressed() {
        MyApplication.showInterstitialAdTrimmer(this,object : FullScreenAdListener(){
            override fun gotoNext() {
                super.gotoNext()
                vibrator!!.cancel()
                mediaPlayer!!.stop()
                finish()
            }
        },"btn_back_m2")

    }

    private fun loadBannerAd() {
        val adId = if (AdConstants.TEST_ADS) {
            getString(R.string.BannerAd)
        } else {
            RemoteConfig.getString(RemoteConfig.MACHINE2_BANNER_AD_ID)
        }
        val makeCollapsible = RemoteConfig.getBoolean(RemoteConfig.MACHINE2_BANNER_MAKE_COLLAPSIBLE)
        val enable = RemoteConfig.getBoolean(RemoteConfig.ENABLE_MACHINE2_BANNER_AD)

        if (paymentSubscription.isPurchased.not() && internetConnection(this) && enable) {
            BannerAd.load(binding.adaptive, adId.trim(), makeCollapsible)
        } else {
            binding.adaptive.visibility = View.GONE
        }
    }
}