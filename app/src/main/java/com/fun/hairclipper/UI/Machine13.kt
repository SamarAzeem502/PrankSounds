package com.`fun`.hairclipper.UI

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Vibrator
import android.view.View
import android.widget.CompoundButton
import com.`fun`.hairclipper.R
import com.`fun`.hairclipper.admobHelper.BannerAd
import com.`fun`.hairclipper.admobHelper.internetConnection
import com.`fun`.hairclipper.databinding.ActivityMachine13Binding

class Machine13 : BaseClass() {
    var vibrator: Vibrator? = null
    var mediaPlayer: MediaPlayer? = null
    private val binding by lazy { ActivityMachine13Binding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        loadBannerAd()
        binding.homeBtn.setOnClickListener { finish() }
        mediaPlayer = MediaPlayer.create(this@Machine13, R.raw.hair_cutter)
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        binding.toggleButton1.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                vibrator!!.vibrate(300000)
                mediaPlayer!!.start()
            } else {
                vibrator!!.cancel()
                mediaPlayer!!.stop()
                mediaPlayer!!.prepareAsync()
            }
        }
        mediaPlayer!!.setOnCompletionListener { mediaPlayer: MediaPlayer? ->
            vibrator!!.cancel()
            binding.toggleButton1.setChecked(false)
        }
    }

    private fun loadBannerAd() {
        if (!paymentSubscription.isPurchased && internetConnection(this)) {
            BannerAd.load(binding.adaptive, getString(R.string.BannerAd), false)
        } else {
            binding.adaptive.visibility = View.GONE
        }
    }
}