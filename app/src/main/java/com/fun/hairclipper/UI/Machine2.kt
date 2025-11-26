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
import com.`fun`.hairclipper.admobHelper.BannerAd
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
        binding.homeBtn.setOnClickListener { finish() }
        loadBannerAd()
        mediaPlayer = MediaPlayer.create(this@Machine2, R.raw.hair_clipper2)
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        binding.toggleButton1.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                vibrator!!.vibrate(300000)
                mediaPlayer!!.start()
            } else {
                vibrator!!.cancel()
                mediaPlayer!!.stop()
                mediaPlayer!!.prepareAsync()
            }
        }

        mediaPlayer!!.setOnCompletionListener { _: MediaPlayer? ->
            vibrator!!.cancel()
            binding.toggleButton1.setChecked(false)
        }
    }

    override fun handleBackPressed() {
        super.handleBackPressed()
        vibrator!!.cancel()
        mediaPlayer!!.stop()
    }

    private fun loadBannerAd() {
        if (!paymentSubscription.isPurchased && internetConnection(this)) {
            BannerAd.load(binding.adaptive, getString(R.string.BannerAd), false)
        } else {
            binding.adaptive.visibility = View.GONE
        }
    }
}