package com.`fun`.hairclipper.UI

import android.annotation.SuppressLint
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import android.widget.Toast
import com.`fun`.hairclipper.R
import com.`fun`.hairclipper.admobHelper.AdConstants
import com.`fun`.hairclipper.admobHelper.BannerAd
import com.`fun`.hairclipper.admobHelper.FullScreenAdListener
import com.`fun`.hairclipper.admobHelper.MyApplication
import com.`fun`.hairclipper.admobHelper.RemoteConfig
import com.`fun`.hairclipper.admobHelper.internetConnection
import com.`fun`.hairclipper.databinding.ActivityHalloweenBinding

class HalloweenActivity : BaseClass() {
    private val binding by lazy { ActivityHalloweenBinding.inflate(layoutInflater) }
    var player: MediaPlayer? = null
    var loopPlayer: MediaPlayer? = null
    private var audioManager: AudioManager? = null
    var soundSelect: String = "bike"
    var loopStatus: Boolean = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        volumeControlStream = AudioManager.STREAM_MUSIC
        loadBanner()
        initControls()
        binding.homeBtn.setOnClickListener { handleBackPressed() }
        binding.lott.animate()
        val shake: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.shake)

        binding.loopButton.apply {
            setText(R.string.start_loop)
            setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_loop, 0, 0, 0)
            setOnClickListener {
                if (!loopStatus) {
                    binding.loopImage.visibility = View.VISIBLE
                    setText(R.string.stop_loop)
                    setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_stop, 0, 0, 0)
                    loopStatus = true
                    binding.lott.visibility = View.VISIBLE

                    player = when (soundSelect) {
                        "bike" -> MediaPlayer.create(this@HalloweenActivity, R.raw.shiver)
                        "police" -> MediaPlayer.create(this@HalloweenActivity, R.raw.creepy)
                        "ambulance" -> MediaPlayer.create(this@HalloweenActivity, R.raw.horror)
                        else -> MediaPlayer.create(this@HalloweenActivity, R.raw.scary)
                    }

                    player!!.start()
                    player!!.isLooping = true
                    binding.imagehiorn.startAnimation(shake)
                    binding.lott.playAnimation()
                } else {
                    MyApplication.showInterstitialAdHalloween(this@HalloweenActivity,object :
                        FullScreenAdListener(){
                        override fun gotoNext() {
                            super.gotoNext()
                            binding.loopImage.visibility = View.GONE
                            setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_loop, 0, 0, 0)
                            setText(R.string.start_loop)
                            loopStatus = false
                            player!!.stop()
                            player!!.release()
                            binding.imagehiorn.clearAnimation()
                            binding.lott.pauseAnimation()
                            binding.lott.visibility = View.GONE
                        }
                    },"btn_stop_loop")

                }
            }
        }

        binding.loopImage.setOnClickListener {
            Toast.makeText(this@HalloweenActivity, R.string.stop_loop_first, Toast.LENGTH_SHORT)
                .show()
        }

        binding.imagehiorn.setOnTouchListener { _, event: MotionEvent? ->
            if (loopStatus) return@setOnTouchListener true

            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.lott.visibility = View.VISIBLE
                    player = when (soundSelect) {
                        "bike" -> MediaPlayer.create(this@HalloweenActivity, R.raw.shiver)
                        "police" -> MediaPlayer.create(this@HalloweenActivity, R.raw.creepy)
                        "ambulance" -> MediaPlayer.create(this@HalloweenActivity, R.raw.horror)
                        else -> MediaPlayer.create(this@HalloweenActivity, R.raw.scary)
                    }
                    player!!.start()
                    player!!.isLooping = true
                    binding.imagehiorn.startAnimation(shake)
                    binding.lott.playAnimation()
                }

                MotionEvent.ACTION_UP -> {
                    player!!.stop()
                    player!!.release()
                    binding.imagehiorn.clearAnimation()
                    binding.lott.pauseAnimation()
                    binding.lott.visibility = View.GONE
                }
            }
            true
        }
    }

    override fun handleBackPressed() {
        MyApplication.showInterstitialAdHalloween(this,object : FullScreenAdListener(){
            override fun gotoNext() {
                super.gotoNext()
                if (loopStatus) {
                    cleanUpMediaPlayer()
                }
                finish()
            }
        },"btn_back_halloween")
    }

    private fun initControls() {
        loopPlayer = MediaPlayer.create(this@HalloweenActivity, R.raw.shiver)
        binding.textSound1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_down)
        binding.imagehiorn.setBackgroundResource(R.drawable.halloween1)
        soundSelect = "bike"

        binding.sound1.setOnClickListener {
            if (!loopStatus) {
                binding.imagehiorn.setBackgroundResource(R.drawable.halloween1)
                loopPlayer = MediaPlayer.create(this@HalloweenActivity, R.raw.shiver)
                binding.textSound1.text = ""
                binding.textSound2.setText(R.string.creeps)
                binding.textSound3.setText(R.string.horror)
                binding.textSound4.setText(R.string.scary)
                binding.textSound1.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    0,
                    R.drawable.ic_down
                )
                binding.textSound2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                binding.textSound3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                binding.textSound4.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                soundSelect = "bike"
            } else {
                Toast.makeText(this, R.string.stop_loop_first, Toast.LENGTH_SHORT).show()
            }
        }

        binding.sound2.setOnClickListener {
            if (!loopStatus) {
                binding.imagehiorn.setBackgroundResource(R.drawable.halloween2)
                loopPlayer = MediaPlayer.create(this@HalloweenActivity, R.raw.creepy)
                binding.textSound1.setText(R.string.shiver)
                binding.textSound2.text = ""
                binding.textSound3.setText(R.string.horror)
                binding.textSound4.setText(R.string.scary)
                binding.textSound1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                binding.textSound2.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    0,
                    R.drawable.ic_down
                )
                binding.textSound3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                binding.textSound4.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                soundSelect = "police"
            } else {
                Toast.makeText(this, R.string.stop_loop_first, Toast.LENGTH_SHORT).show()
            }
        }

        binding.sound3.setOnClickListener {
            if (!loopStatus) {
                binding.imagehiorn.setBackgroundResource(R.drawable.halloween3)
                loopPlayer = MediaPlayer.create(this@HalloweenActivity, R.raw.horror)
                binding.textSound1.setText(R.string.shiver)
                binding.textSound2.setText(R.string.creeps)
                binding.textSound3.text = ""
                binding.textSound4.setText(R.string.scary)
                binding.textSound1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                binding.textSound2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                binding.textSound3.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    0,
                    R.drawable.ic_down
                )
                binding.textSound4.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                soundSelect = "ambulance"
            } else {
                Toast.makeText(this, R.string.stop_loop_first, Toast.LENGTH_SHORT).show()
            }
        }

        binding.sound4.setOnClickListener {
            if (!loopStatus) {
                binding.imagehiorn.setBackgroundResource(R.drawable.halloween4)
                loopPlayer = MediaPlayer.create(this@HalloweenActivity, R.raw.scary)
                binding.textSound1.setText(R.string.shiver)
                binding.textSound2.setText(R.string.creeps)
                binding.textSound3.setText(R.string.horror)
                binding.textSound4.text = ""
                binding.textSound1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                binding.textSound2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                binding.textSound3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                binding.textSound4.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    0,
                    R.drawable.ic_down
                )
                soundSelect = "truck"
            } else {
                Toast.makeText(this, R.string.stop_loop_first, Toast.LENGTH_SHORT).show()
            }
        }

        binding.volumeLow.setOnClickListener { binding.seekbar1.progress -= 1 }
        binding.volumeUp.setOnClickListener { binding.seekbar1.progress += 1 }

        try {
            audioManager = getSystemService(AUDIO_SERVICE) as? AudioManager
            audioManager?.let { am ->
                binding.seekbar1.max = 20
                binding.seekbar1.progress = 15
                val pro = binding.seekbar1.progress
                am.setStreamVolume(AudioManager.STREAM_MUSIC, pro, 0)

                binding.seekbar1.setOnSeekBarChangeListener(object :
                    SeekBar.OnSeekBarChangeListener {
                    override fun onStopTrackingTouch(arg0: SeekBar?) {}
                    override fun onStartTrackingTouch(arg0: SeekBar?) {}
                    override fun onProgressChanged(arg0: SeekBar?, progress: Int, arg2: Boolean) {
                        am.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
                    }
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadBanner() {
        val adId = if (AdConstants.TEST_ADS) {
            getString(R.string.BannerAd)
        } else {
            RemoteConfig.getString(RemoteConfig.HALLOWEEN_BANNER_AD_ID)
        }
        val makeCollapsible = RemoteConfig.getBoolean(RemoteConfig.HALLOWEEN_BANNER_MAKE_COLLAPSIBLE)
        val enable = RemoteConfig.getBoolean(RemoteConfig.ENABLE_HALLOWEEN_BANNER_AD)

        if (paymentSubscription.isPurchased.not() && internetConnection(this) && enable) {
            BannerAd.load(binding.adaptiveHalloween, adId.trim(), makeCollapsible)
        } else {
            binding.adaptiveHalloween.visibility = View.GONE
        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP -> {
                binding.seekbar1.progress += 1
                return true
            }

            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                binding.seekbar1.progress -= 1
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    fun cleanUpMediaPlayer() {
        if (player != null) {
            if (player!!.isPlaying) {
                player!!.stop()
            }
            player!!.release()
            player = null
        }
    }
}