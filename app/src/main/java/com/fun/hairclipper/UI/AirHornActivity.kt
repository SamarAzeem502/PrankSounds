package com.`fun`.hairclipper.UI

import android.annotation.SuppressLint
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import android.widget.Toast
import com.`fun`.hairclipper.R
import com.`fun`.hairclipper.admobHelper.BannerAd
import com.`fun`.hairclipper.admobHelper.internetConnection
import com.`fun`.hairclipper.databinding.ActivityAirHornBinding

class AirHornActivity : BaseClass() {
    private val binding by lazy { ActivityAirHornBinding.inflate(layoutInflater) }
    private var player: MediaPlayer? = null
    var loopPlayer: MediaPlayer? = null
    private var audioManager: AudioManager? = null
    var soundSelect: String = "police"
    var loopStatus: Boolean = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        volumeControlStream = AudioManager.STREAM_MUSIC
        loadBanner()
        initControls()
        val shake = AnimationUtils.loadAnimation(applicationContext, R.anim.shake)
        binding.lott.animate()
        binding.homeBtn.setOnClickListener { finish() }
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
                        "bike" -> MediaPlayer.create(this@AirHornActivity, R.raw.bike)
                        "police" -> MediaPlayer.create(this@AirHornActivity, R.raw.police)
                        "ambulance" -> MediaPlayer.create(this@AirHornActivity, R.raw.ambulance)
                        else -> MediaPlayer.create(this@AirHornActivity, R.raw.truck)
                    }

                    player?.isLooping = true
                    player?.start()
                    binding.imagehiorn.startAnimation(shake)
                    binding.lott.playAnimation()
                } else {
                    binding.loopImage.visibility = View.GONE
                    setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_loop, 0, 0, 0)
                    setText(R.string.start_loop)
                    loopStatus = false

                    player?.stop()
                    player?.release()
                    player = null // Clear the reference

                    binding.imagehiorn.clearAnimation()
                    binding.lott.pauseAnimation()
                    binding.lott.visibility = View.GONE
                }
            }
        }

        binding.loopImage.setOnClickListener {
            Toast.makeText(this@AirHornActivity, R.string.stop_loop_first, Toast.LENGTH_SHORT)
                .show()
        }

        binding.imagehiorn.setOnTouchListener { _, event: MotionEvent? ->
            if (loopStatus) return@setOnTouchListener true

            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.lott.visibility = View.VISIBLE
                    player = when (soundSelect) {
                        "bike" -> MediaPlayer.create(this@AirHornActivity, R.raw.bike)
                        "police" -> MediaPlayer.create(this@AirHornActivity, R.raw.police)
                        "ambulance" -> MediaPlayer.create(this@AirHornActivity, R.raw.ambulance)
                        else -> MediaPlayer.create(this@AirHornActivity, R.raw.truck)
                    }

                    player?.isLooping = true
                    player?.start()
                    binding.imagehiorn.startAnimation(shake)
                    binding.lott.playAnimation()
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    player?.stop()
                    player?.release()
                    player = null // Clear the reference

                    binding.imagehiorn.clearAnimation()
                    binding.lott.pauseAnimation()
                    binding.lott.visibility = View.GONE
                }
            }
            true
        }
    }

    override fun handleBackPressed() {
        if (loopStatus) {
            cleanUpMediaPlayer()
        }
        super.handleBackPressed()
    }

    private fun initControls() {
        loopPlayer = MediaPlayer.create(this@AirHornActivity, R.raw.bike)
        binding.textSound2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_down)
        binding.imagehiorn.setBackgroundResource(R.drawable.vehicle2)
        soundSelect = "police"

        binding.sound1.setOnClickListener {
            if (!loopStatus) {
                binding.imagehiorn.setBackgroundResource(R.drawable.vehicle1)
                loopPlayer = MediaPlayer.create(this@AirHornActivity, R.raw.bike)
                binding.textSound1.text = ""
                binding.textSound2.setText(R.string.police)
                binding.textSound3.setText(R.string.ambulance)
                binding.textSound4.setText(R.string.truck)
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
                binding.imagehiorn.setBackgroundResource(R.drawable.vehicle2)
                loopPlayer = MediaPlayer.create(this@AirHornActivity, R.raw.police)
                binding.textSound1.setText(R.string.bike)
                binding.textSound2.text = ""
                binding.textSound3.setText(R.string.ambulance)
                binding.textSound4.setText(R.string.truck)
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
                binding.imagehiorn.setBackgroundResource(R.drawable.vehicle3)
                loopPlayer = MediaPlayer.create(this@AirHornActivity, R.raw.ambulance)
                binding.textSound1.setText(R.string.bike)
                binding.textSound2.setText(R.string.police)
                binding.textSound3.text = ""
                binding.textSound4.setText(R.string.truck)
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
                binding.imagehiorn.setBackgroundResource(R.drawable.vehicle4)
                loopPlayer = MediaPlayer.create(this@AirHornActivity, R.raw.truck)
                binding.textSound1.setText(R.string.bike)
                binding.textSound2.setText(R.string.police)
                binding.textSound3.setText(R.string.ambulance)
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

        binding.volumeLow.setOnClickListener {
            binding.seekbar1.progress -= 1
        }
        binding.volumeUp.setOnClickListener {
            binding.seekbar1.progress += 1
        }

        try {
            audioManager = getSystemService(AUDIO_SERVICE) as? AudioManager
            audioManager?.let { am ->
                // The original code set the max/progress twice. This version keeps the second set of hardcoded values to preserve functionality.
                binding.seekbar1.max = 20
                binding.seekbar1.progress = 15
                am.setStreamVolume(AudioManager.STREAM_MUSIC, 15, 0)

                binding.seekbar1.setOnSeekBarChangeListener(object :
                    SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        am.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                })
            }
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

    private fun cleanUpMediaPlayer() {
        player?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }
        player = null
    }

    private fun loadBanner() {
        if (paymentSubscription.isPurchased.not() && internetConnection(this)) {
            BannerAd.load(binding.adaptiveHorn, getString(R.string.BannerAd), false)
        } else {
            binding.adaptiveHorn.visibility = View.GONE
        }
    }
}