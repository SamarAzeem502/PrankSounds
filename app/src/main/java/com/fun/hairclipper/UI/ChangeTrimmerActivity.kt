package com.`fun`.hairclipper.UI

import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.view.View
import com.`fun`.hairclipper.R
import com.`fun`.hairclipper.admobHelper.BannerAd
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

        binding.homeBtn.setOnClickListener { finish() }
        binding.machine1.setOnClickListener { startActivity(Intent(this, Machine1::class.java)) }
        binding.machine2.setOnClickListener { startActivity(Intent(this, Machine2::class.java)) }
        binding.machine3.setOnClickListener { startActivity(Intent(this, Machine3::class.java)) }
        binding.machine4.setOnClickListener { startActivity(Intent(this, Machine4::class.java)) }
        binding.machine5.setOnClickListener { startActivity(Intent(this, Machine5::class.java)) }
        binding.machine6.setOnClickListener { startActivity(Intent(this, Machine6::class.java)) }
        binding.machine7.setOnClickListener { startActivity(Intent(this, Machine7::class.java)) }
        binding.machine8.setOnClickListener { startActivity(Intent(this, Machine8::class.java)) }
        binding.machine9.setOnClickListener { startActivity(Intent(this, Machine9::class.java)) }
        binding.machine10.setOnClickListener { startActivity(Intent(this, Machine10::class.java)) }
        binding.machine11.setOnClickListener { startActivity(Intent(this, Machine11::class.java)) }
        binding.machine12.setOnClickListener { startActivity(Intent(this, Machine12::class.java)) }
        binding.machine13.setOnClickListener { startActivity(Intent(this, Machine13::class.java)) }
    }

    private fun loadCollapsibleBanner() {
        if (!paymentSubscription.isPurchased && internetConnection(this)) {
            BannerAd.loadTop(binding.adaptivemain, getString(R.string.CollapsibleBannerAd), true)
        } else {
            binding.adaptivemain.visibility = View.GONE
        }
    }

    private fun setVolumeMax() {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 1, 0)
    }
}