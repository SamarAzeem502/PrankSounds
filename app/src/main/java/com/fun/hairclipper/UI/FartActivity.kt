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
import com.`fun`.hairclipper.databinding.ActivityFartBinding

class FartActivity : BaseClass() {
    private val binding by lazy { ActivityFartBinding.inflate(layoutInflater) }
    private var player: MediaPlayer? = null
    private var loopPlayer: MediaPlayer? = null
    private lateinit var audioManager: AudioManager
    private lateinit var shakeAnimation: Animation
    private var soundSelect: String = "bike"
    private var loopStatus: Boolean = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        volumeControlStream = AudioManager.STREAM_MUSIC
        shakeAnimation = AnimationUtils.loadAnimation(applicationContext, R.anim.shake)
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        loadBanner()
        initControls()
        setupMainActionListeners()
    }


    private fun setupMainActionListeners() {
        binding.loopButton.apply {
            setText(R.string.start_loop)
            setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_loop, 0, 0, 0)
            setOnClickListener {
                if (!loopStatus) {
                    // Start Loop
                    binding.loopImage.visibility = View.VISIBLE
                    setText(R.string.stop_loop)
                    setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_stop, 0, 0, 0)
                    loopStatus = true
                    startSoundAndAnimation(isLoopingAudio = true)
                } else {
                    MyApplication.showInterstitialAdFart(
                        this@FartActivity,
                        object : FullScreenAdListener() {
                            override fun gotoNext() {
                                super.gotoNext()
                                // Stop Loop
                                binding.loopImage.visibility = View.GONE
                                setText(R.string.start_loop)
                                setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_loop, 0, 0, 0)
                                loopStatus = false
                                stopSoundAndAnimation()
                            }
                        },
                        "stop_loop"
                    )

                }
            }
        }

        binding.homeBtn.setOnClickListener {
            MyApplication.showInterstitialAdFart(this, object : FullScreenAdListener() {
                override fun gotoNext() {
                    super.gotoNext()
                    finish()
                }
            }, "btn_fart_back")

        }

        binding.loopImage.setOnClickListener {
            Toast.makeText(this, R.string.stop_loop_first, Toast.LENGTH_SHORT).show()
        }

        binding.imagehiorn.setOnTouchListener { _, event ->
            if (loopStatus) return@setOnTouchListener true

            when (event.action) {
                MotionEvent.ACTION_DOWN -> startSoundAndAnimation(isLoopingAudio = true)
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> stopSoundAndAnimation()
            }
            true
        }
    }

    private fun startSoundAndAnimation(isLoopingAudio: Boolean) {
        stopAndReleasePlayer()
        player = createPlayerForSelectedSound()

        player?.let {
            it.isLooping = isLoopingAudio
            it.start()
            binding.lott.visibility = View.VISIBLE
            binding.lott.playAnimation()
            binding.imagehiorn.startAnimation(shakeAnimation)
        }
    }

    private fun stopSoundAndAnimation() {
        stopAndReleasePlayer()
        binding.imagehiorn.clearAnimation()
        binding.lott.pauseAnimation()
        binding.lott.visibility = View.GONE
    }

    private fun createPlayerForSelectedSound(): MediaPlayer? {
        val resourceId = when (soundSelect) {
            "bike" -> R.raw.bubbles
            "police" -> R.raw.poop
            "ambulance" -> R.raw.wet
            "truck" -> R.raw.dripping
            else -> R.raw.dripping
        }
        return MediaPlayer.create(this, resourceId)
    }

    override fun handleBackPressed() {
        MyApplication.showInterstitialAdFart(this, object : FullScreenAdListener() {
            override fun gotoNext() {
                super.gotoNext()
                stopAndReleasePlayer()
                finish()
            }
        }, "fart_back")

    }

    private fun initControls() {
        val textViews = listOf(
            binding.textSound1,
            binding.textSound2,
            binding.textSound3,
            binding.textSound4
        )

        fun updateSelectionUI(
            selectedIndex: Int,
            selectedSound: String,
            imageRes: Int,
            soundRes: Int
        ) {
            if (loopStatus) {
                Toast.makeText(this, R.string.stop_loop_first, Toast.LENGTH_SHORT).show()
                return
            }

            soundSelect = selectedSound
            binding.imagehiorn.setBackgroundResource(imageRes)
            loopPlayer?.release()
            loopPlayer = MediaPlayer.create(this, soundRes)

            val stringResources =
                listOf(R.string.bubbles, R.string.poop, R.string.wet_fart, R.string.dripping)
            textViews.forEachIndexed { index, textView ->
                if (index == selectedIndex) {
                    textView.text = ""
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_down)
                } else {
                    textView.setText(stringResources[index])
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                }
            }
        }

        updateSelectionUI(0, "bike", R.drawable.fart1, R.raw.bubbles)

        binding.sound1.setOnClickListener {
            updateSelectionUI(
                0,
                "bike",
                R.drawable.fart1,
                R.raw.bubbles
            )
        }
        binding.sound2.setOnClickListener {
            updateSelectionUI(
                1,
                "police",
                R.drawable.fart2,
                R.raw.poop
            )
        }
        binding.sound3.setOnClickListener {
            updateSelectionUI(
                2,
                "ambulance",
                R.drawable.fart3,
                R.raw.wet
            )
        }
        binding.sound4.setOnClickListener {
            updateSelectionUI(
                3,
                "truck",
                R.drawable.fart4,
                R.raw.dripping
            )
        }

        setupVolumeControls()
    }

    private fun setupVolumeControls() {
        binding.volumeLow.setOnClickListener {
            binding.seekbar1.progress -= 1
        }
        binding.volumeUp.setOnClickListener {
            binding.seekbar1.progress += 1
        }

        try {
            binding.seekbar1.max = 20
            binding.seekbar1.progress = 15
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 15, 0)

            binding.seekbar1.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        } catch (e: Exception) {
            e.printStackTrace()
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

    private fun stopAndReleasePlayer() {
        player?.apply {
            if (isPlaying) stop()
            release()
        }
        player = null
    }

    private fun loadBanner() {
        val adId = if (AdConstants.TEST_ADS) {
            getString(R.string.BannerAd)
        } else {
            RemoteConfig.getString(RemoteConfig.FART_BANNER_AD_ID)
        }
        val makeCollapsible = RemoteConfig.getBoolean(RemoteConfig.FART_BANNER_MAKE_COLLAPSIBLE)
        val enable = RemoteConfig.getBoolean(RemoteConfig.ENABLE_FART_BANNER_AD)

        if (paymentSubscription.isPurchased.not() && internetConnection(this) && enable) {
            BannerAd.load(binding.adaptiveFart, adId.trim(), makeCollapsible)
        } else {
            binding.adaptiveFart.visibility = View.GONE
        }
    }
}