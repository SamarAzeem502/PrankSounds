package com.`fun`.hairclipper.UI

import android.annotation.SuppressLint
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.airbnb.lottie.LottieAnimationView
import com.`fun`.hairclipper.R
import com.`fun`.hairclipper.admobHelper.AdConstants
import com.`fun`.hairclipper.admobHelper.BannerAd
import com.`fun`.hairclipper.admobHelper.FullScreenAdListener
import com.`fun`.hairclipper.admobHelper.MyApplication
import com.`fun`.hairclipper.admobHelper.RemoteConfig
import com.`fun`.hairclipper.admobHelper.internetConnection

class StunGunActivity : BaseClass() {
    var player: MediaPlayer? = null
    var loopPlayer: MediaPlayer? = null
    private var volumeSeekbar: SeekBar? = null
    private var audioManager: AudioManager? = null
    var soundSelect: String = "bike"
    var loopStatus: Boolean = false
    var imageView: ImageView? = null
    var loopImageView: ImageView? = null
    private var frameLayout: FrameLayout? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stun_gun)
        volumeControlStream = AudioManager.STREAM_MUSIC
        imageView = findViewById(R.id.imagehiorn)
        loopImageView = findViewById(R.id.loopImage)
        frameLayout = findViewById(R.id.adaptiveStun)
        loadBannerAd()

        initControls()
        val splashScreeny: LottieAnimationView = findViewById(R.id.lott)
        splashScreeny.animate()
        val shake = AnimationUtils.loadAnimation(applicationContext, R.anim.shake)
        val home = findViewById<ImageView>(R.id.home_btn)
        home.setOnClickListener { handleBackPressed() }
        val loopButton = findViewById<Button>(R.id.loopButton)
        loopButton.setText(R.string.start_loop)
        loopButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_loop, 0, 0, 0)
        loopButton.setOnClickListener {
            if (!loopStatus) {
                loopImageView!!.visibility = View.VISIBLE
                loopButton.setText(R.string.stop_loop)
                loopButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_stop, 0, 0, 0)
                loopStatus = true
                splashScreeny.visibility = View.VISIBLE
                player = when (soundSelect) {
                    "bike" -> {
                        MediaPlayer.create(this@StunGunActivity, R.raw.stun1)
                    }

                    "police" -> {
                        MediaPlayer.create(this@StunGunActivity, R.raw.stun2)
                    }

                    "ambulance" -> {
                        MediaPlayer.create(this@StunGunActivity, R.raw.stun3)
                    }

                    else -> {
                        MediaPlayer.create(this@StunGunActivity, R.raw.stun4)
                    }
                }

                player!!.start()
                player!!.isLooping = true
                imageView!!.startAnimation(shake)
                splashScreeny.playAnimation()
            } else {
                MyApplication.showInterstitialAdStunt(
                    this@StunGunActivity,
                    object : FullScreenAdListener() {
                        override fun gotoNext() {
                            super.gotoNext()
                            loopImageView!!.visibility = View.GONE
                            loopButton.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.ic_loop,
                                0,
                                0,
                                0
                            )
                            loopButton.setText(R.string.start_loop)
                            loopStatus = false
                            player!!.stop()
                            player!!.release()
                            imageView!!.clearAnimation()
                            splashScreeny.pauseAnimation()
                            splashScreeny.visibility = View.GONE
                        }
                    },
                    "btn_stop_loop"
                )

            }
        }

        loopImageView!!.setOnClickListener {
            Toast.makeText(
                this@StunGunActivity,
                R.string.stop_loop_first,
                Toast.LENGTH_SHORT
            ).show()
        }
        imageView!!.setOnTouchListener { _: View?, event: MotionEvent? ->
            if (event?.action == MotionEvent.ACTION_DOWN) {
                splashScreeny.visibility = View.VISIBLE
                player = when (soundSelect) {
                    "bike" -> {
                        MediaPlayer.create(this@StunGunActivity, R.raw.stun1)
                    }

                    "police" -> {
                        MediaPlayer.create(this@StunGunActivity, R.raw.stun2)
                    }

                    "ambulance" -> {
                        MediaPlayer.create(this@StunGunActivity, R.raw.stun3)
                    }

                    else -> {
                        MediaPlayer.create(this@StunGunActivity, R.raw.stun4)
                    }
                }

                player!!.start()
                player!!.isLooping = true
                imageView!!.startAnimation(shake)
                splashScreeny.playAnimation()
            } else if (event?.action == MotionEvent.ACTION_UP) {
                player!!.stop()
                player!!.release()
                imageView!!.clearAnimation()
                splashScreeny.pauseAnimation()
                splashScreeny.visibility = View.GONE
            }
            true
        }
    }

    override fun handleBackPressed() {
        MyApplication.showInterstitialAdStunt(this, object : FullScreenAdListener() {
            override fun gotoNext() {
                super.gotoNext()
                cleanUpMediaPlayer()
                finish()
            }
        }, "btn_back_stunt")
    }

    private fun initControls() {
        loopPlayer = MediaPlayer.create(this@StunGunActivity, R.raw.stun1)

        val card1: CardView = findViewById(R.id.sound1)
        val card2: CardView = findViewById(R.id.sound2)
        val card3: CardView = findViewById(R.id.sound3)
        val card4: CardView = findViewById(R.id.sound4)

        val text1: TextView = findViewById(R.id.text_sound1)
        val text2: TextView = findViewById(R.id.text_sound2)
        val text3: TextView = findViewById(R.id.text_sound3)
        val text4: TextView = findViewById(R.id.text_sound4)

        text1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_down)
        imageView!!.setBackgroundResource(R.drawable.stun1)
        soundSelect = "bike"
        card1.setOnClickListener {
            if (!loopStatus) {
                imageView!!.setBackgroundResource(R.drawable.stun1)
                loopPlayer = MediaPlayer.create(this@StunGunActivity, R.raw.stun1)
                text1.text = ""
                text2.setText(R.string.vipertek)
                text3.setText(R.string.tactical)
                text4.setText(R.string.taser)
                text1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_down)
                text2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                text3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                text4.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                soundSelect = "bike"
            } else {
                Toast.makeText(this, R.string.stop_loop_first, Toast.LENGTH_SHORT).show()
            }
        }
        card2.setOnClickListener {
            if (!loopStatus) {
                imageView!!.setBackgroundResource(R.drawable.stun2)
                loopPlayer = MediaPlayer.create(this@StunGunActivity, R.raw.stun2)
                text1.setText(R.string.stun_gun)
                text2.text = ""
                text3.setText(R.string.tactical)
                text4.setText(R.string.taser)
                text1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                text2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_down)
                text3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                text4.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                soundSelect = "police"
            } else {
                Toast.makeText(this, R.string.stop_loop_first, Toast.LENGTH_SHORT).show()
            }
        }
        card3.setOnClickListener {
            if (!loopStatus) {
                imageView!!.setBackgroundResource(R.drawable.stun3)
                loopPlayer = MediaPlayer.create(this@StunGunActivity, R.raw.stun3)
                text1.setText(R.string.stun_gun)
                text2.setText(R.string.vipertek)
                text3.text = ""
                text4.setText(R.string.taser)
                text1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                text2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                text3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_down)
                text4.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                soundSelect = "ambulance"
            } else {
                Toast.makeText(this, R.string.stop_loop_first, Toast.LENGTH_SHORT).show()
            }
        }
        card4.setOnClickListener {
            if (!loopStatus) {
                imageView!!.setBackgroundResource(R.drawable.stun4)
                loopPlayer = MediaPlayer.create(this@StunGunActivity, R.raw.stun4)
                text1.setText(R.string.stun_gun)
                text2.setText(R.string.vipertek)
                text3.setText(R.string.tactical)
                text4.text = ""
                text1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                text2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                text3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                text4.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_down)
                soundSelect = "truck"
            } else {
                Toast.makeText(this, R.string.stop_loop_first, Toast.LENGTH_SHORT).show()
            }
        }

        val low = findViewById<ImageView>(R.id.volumeLow)
        low.setOnClickListener {
            val index = volumeSeekbar!!.progress
            volumeSeekbar!!.progress = index - 1
        }
        val high = findViewById<ImageView>(R.id.volumeUp)
        high.setOnClickListener {
            val index = volumeSeekbar!!.progress
            volumeSeekbar!!.progress = index + 1
        }
        try {
            volumeSeekbar = findViewById<View?>(R.id.seekbar1) as SeekBar?
            audioManager = getSystemService(AUDIO_SERVICE) as AudioManager?

            volumeSeekbar!!.max = audioManager!!
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            volumeSeekbar!!.progress = audioManager!!
                .getStreamVolume(AudioManager.STREAM_MUSIC)
            volumeSeekbar!!.max = 20
            volumeSeekbar!!.progress = 15
            val pro = volumeSeekbar!!.progress

            audioManager!!.setStreamVolume(AudioManager.STREAM_MUSIC, pro, 0)

            volumeSeekbar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(arg0: SeekBar?) {
                }

                override fun onStartTrackingTouch(arg0: SeekBar?) {
                }

                override fun onProgressChanged(arg0: SeekBar?, progress: Int, arg2: Boolean) {
                    audioManager!!.setStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        progress, 0
                    )
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun loadBannerAd() {
        val adId = if (AdConstants.TEST_ADS) {
            getString(R.string.BannerAd)
        } else {
            RemoteConfig.getString(RemoteConfig.STUNT_GUN_BANNER_AD_ID)
        }
        val makeCollapsible =
            RemoteConfig.getBoolean(RemoteConfig.STUNT_GUN_BANNER_MAKE_COLLAPSIBLE)
        val enable = RemoteConfig.getBoolean(RemoteConfig.ENABLE_STUNT_GUN_BANNER_AD)

        if (paymentSubscription.isPurchased.not() && internetConnection(this) && enable) {
            BannerAd.load(frameLayout!!, adId.trim(), makeCollapsible)
        } else {
            frameLayout!!.visibility = View.GONE
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            // mediaVlmSeekBar = (SeekBar) findViewById(R.id.seekBar1);
            val index = volumeSeekbar!!.progress
            volumeSeekbar!!.progress = index + 1
            return true
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            val index = volumeSeekbar!!.progress
            volumeSeekbar!!.progress = index - 1
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    fun cleanUpMediaPlayer() {
        try {
            player?.let {
                try {
                    if (it.isPlaying) {
                        it.stop()
                    }
                } catch (e: IllegalStateException) {
                    // MediaPlayer was not in a valid state, ignore
                }
                it.release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            player = null
        }
    }

}