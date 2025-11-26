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
import com.`fun`.hairclipper.admobHelper.BannerAd
import com.`fun`.hairclipper.admobHelper.internetConnection
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.interstitial.InterstitialAd

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
    private var adView: AdView? = null
    private val mInterstitialAd: InterstitialAd? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stun_gun)
        volumeControlStream = AudioManager.STREAM_MUSIC
        imageView = findViewById<ImageView>(R.id.imagehiorn)
        loopImageView = findViewById<ImageView>(R.id.loopImage)
        frameLayout = findViewById<FrameLayout>(R.id.adaptiveStun)
        loadBanner()

        initControls()
        val splashscreeny: LottieAnimationView = findViewById<LottieAnimationView>(R.id.lott)
        splashscreeny.animate()
        val shake = AnimationUtils.loadAnimation(applicationContext, R.anim.shake)
        val home = findViewById<ImageView>(R.id.home_btn)
        home.setOnClickListener { finish() }
        val loopButton = findViewById<Button>(R.id.loopButton)
        loopButton.setText(R.string.start_loop)
        loopButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_loop, 0, 0, 0)
        loopButton.setOnClickListener(View.OnClickListener { v: View? ->
            if (!loopStatus) {
                loopImageView!!.setVisibility(View.VISIBLE)
                loopButton.setText(R.string.stop_loop)
                loopButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_stop, 0, 0, 0)
                loopStatus = true
                splashscreeny.setVisibility(View.VISIBLE)
                if (soundSelect == "bike") {
                    player = MediaPlayer.create(this@StunGunActivity, R.raw.stun1)
                } else if (soundSelect == "police") {
                    player = MediaPlayer.create(this@StunGunActivity, R.raw.stun2)
                } else if (soundSelect == "ambulance") {
                    player = MediaPlayer.create(this@StunGunActivity, R.raw.stun3)
                } else {
                    player = MediaPlayer.create(this@StunGunActivity, R.raw.stun4)
                }

                player!!.start()
                player!!.isLooping = true
                imageView!!.startAnimation(shake)
                splashscreeny.playAnimation()
            } else {
                loopImageView!!.setVisibility(View.GONE)
                loopButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_loop, 0, 0, 0)
                loopButton.setText(R.string.start_loop)
                loopStatus = false
                player!!.stop()
                player!!.release()
                imageView!!.clearAnimation()
                splashscreeny.pauseAnimation()
                splashscreeny.setVisibility(View.GONE)
            }
        })

        loopImageView!!.setOnClickListener(View.OnClickListener { v: View? ->
            Toast.makeText(
                this@StunGunActivity,
                R.string.stop_loop_first,
                Toast.LENGTH_SHORT
            ).show()
        })
        imageView!!.setOnTouchListener(View.OnTouchListener { v: View?, event: MotionEvent? ->
            if (event?.getAction() == MotionEvent.ACTION_DOWN) {
                splashscreeny.setVisibility(View.VISIBLE)
                if (soundSelect == "bike") {
                    player = MediaPlayer.create(this@StunGunActivity, R.raw.stun1)
                } else if (soundSelect == "police") {
                    player = MediaPlayer.create(this@StunGunActivity, R.raw.stun2)
                } else if (soundSelect == "ambulance") {
                    player = MediaPlayer.create(this@StunGunActivity, R.raw.stun3)
                } else {
                    player = MediaPlayer.create(this@StunGunActivity, R.raw.stun4)
                }

                player!!.start()
                player!!.isLooping = true
                imageView!!.startAnimation(shake)
                splashscreeny.playAnimation()
            } else if (event?.getAction() == MotionEvent.ACTION_UP) {
                player!!.stop()
                player!!.release()
                imageView!!.clearAnimation()
                splashscreeny.pauseAnimation()
                splashscreeny.setVisibility(View.GONE)
            }
            true
        })
    }

    override fun handleBackPressed() {
        if (loopStatus) {
            cleanUpMediaPlayer()
        }
        super.handleBackPressed()
    }

    private fun initControls() {
        loopPlayer = MediaPlayer.create(this@StunGunActivity, R.raw.stun1)

        val card1: CardView = findViewById<CardView>(R.id.sound1)
        val card2: CardView = findViewById<CardView>(R.id.sound2)
        val card3: CardView = findViewById<CardView>(R.id.sound3)
        val card4: CardView = findViewById<CardView>(R.id.sound4)

        val text1: TextView = findViewById<TextView>(R.id.text_sound1)
        val text2: TextView = findViewById<TextView>(R.id.text_sound2)
        val text3: TextView = findViewById<TextView>(R.id.text_sound3)
        val text4: TextView = findViewById<TextView>(R.id.text_sound4)

        text1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_down)
        imageView!!.setBackgroundResource(R.drawable.stun1)
        soundSelect = "bike"
        card1.setOnClickListener(View.OnClickListener { v: View? ->
            if (!loopStatus) {
                imageView!!.setBackgroundResource(R.drawable.stun1)
                loopPlayer = MediaPlayer.create(this@StunGunActivity, R.raw.stun1)
                text1.setText("")
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
        })
        card2.setOnClickListener(View.OnClickListener { v: View? ->
            if (!loopStatus) {
                imageView!!.setBackgroundResource(R.drawable.stun2)
                loopPlayer = MediaPlayer.create(this@StunGunActivity, R.raw.stun2)
                text1.setText(R.string.stun_gun)
                text2.setText("")
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
        })
        card3.setOnClickListener(View.OnClickListener { v: View? ->
            if (!loopStatus) {
                imageView!!.setBackgroundResource(R.drawable.stun3)
                loopPlayer = MediaPlayer.create(this@StunGunActivity, R.raw.stun3)
                text1.setText(R.string.stun_gun)
                text2.setText(R.string.vipertek)
                text3.setText("")
                text4.setText(R.string.taser)
                text1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                text2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                text3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_down)
                text4.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                soundSelect = "ambulance"
            } else {
                Toast.makeText(this, R.string.stop_loop_first, Toast.LENGTH_SHORT).show()
            }
        })
        card4.setOnClickListener(View.OnClickListener { v: View? ->
            if (!loopStatus) {
                imageView!!.setBackgroundResource(R.drawable.stun4)
                loopPlayer = MediaPlayer.create(this@StunGunActivity, R.raw.stun4)
                text1.setText(R.string.stun_gun)
                text2.setText(R.string.vipertek)
                text3.setText(R.string.tactical)
                text4.setText("")
                text1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                text2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                text3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                text4.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_down)
                soundSelect = "truck"
            } else {
                Toast.makeText(this, R.string.stop_loop_first, Toast.LENGTH_SHORT).show()
            }
        })

        val low = findViewById<ImageView>(R.id.volumeLow)
        low.setOnClickListener(View.OnClickListener { v: View? ->
            val index = volumeSeekbar!!.progress
            volumeSeekbar!!.progress = index - 1
        })
        val high = findViewById<ImageView>(R.id.volumeUp)
        high.setOnClickListener(View.OnClickListener { v: View? ->
            val index = volumeSeekbar!!.progress
            volumeSeekbar!!.progress = index + 1
        })
        try {
            volumeSeekbar = findViewById<View?>(R.id.seekbar1) as SeekBar?
            audioManager = getSystemService(AUDIO_SERVICE) as AudioManager?

            volumeSeekbar!!.setMax(
                audioManager!!
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            )
            volumeSeekbar!!.progress = audioManager!!
                .getStreamVolume(AudioManager.STREAM_MUSIC)
            volumeSeekbar!!.setMax(20)
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

    private fun loadBanner() {
        if (!paymentSubscription.isPurchased && internetConnection(this)) {
            BannerAd.load(frameLayout!!, getString(R.string.BannerAd), false)
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
        if (player != null) {
            if (player!!.isPlaying) {
                player!!.stop()
            }
            player!!.release()
        }
    }
}