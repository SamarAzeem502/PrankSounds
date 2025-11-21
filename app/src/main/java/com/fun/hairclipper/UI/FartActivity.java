package com.fun.hairclipper.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.airbnb.lottie.LottieAnimationView;
import com.fun.hairclipper.R;
import com.fun.hairclipper.admobHelper.MyApplication;
import com.fun.hairclipper.tools.Tool;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.interstitial.InterstitialAd;

import java.util.Objects;

public class FartActivity extends BaseClass {
    MediaPlayer player, loopPlayer;
    private SeekBar volumeSeekbar = null;
    private AudioManager audioManager = null;
    String soundSelect = "bike";
    Boolean loopStatus = false;
    ImageView imageView, loopImageView;
    private FrameLayout frameLayout;
    private InterstitialAd mInterstitialAd;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fart);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        imageView = findViewById(R.id.imagehiorn);
        loopImageView = findViewById(R.id.loopImage);
        frameLayout = findViewById(R.id.adaptiveFart);
        if (!getPaymentSubscription().isPurchased() && Tool.isNetworkAvailable(this)) {
            loadBanner();
        } else {
            frameLayout.setVisibility(View.GONE);
        }
        initControls();
        LottieAnimationView splashscreeny;
        splashscreeny = findViewById(R.id.lott);
        splashscreeny.animate();
        Animation shake;
        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        Button loopButton = findViewById(R.id.loopButton);
        loopButton.setText(R.string.start_loop);
        loopButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_loop, 0, 0, 0);
        loopButton.setOnClickListener(v -> {

            if (!loopStatus) {
                loopImageView.setVisibility(View.VISIBLE);
                loopButton.setText(R.string.stop_loop);
                loopButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_stop, 0, 0, 0);
                loopStatus = true;
                splashscreeny.setVisibility(View.VISIBLE);
                if (soundSelect.equals("bike")) {
                    player = MediaPlayer.create(FartActivity.this, R.raw.bubbles);
                } else if (soundSelect.equals("police")) {
                    player = MediaPlayer.create(FartActivity.this, R.raw.poop);
                } else if (soundSelect.equals("ambulance")) {
                    player = MediaPlayer.create(FartActivity.this, R.raw.wet);
                } else {
                    player = MediaPlayer.create(FartActivity.this, R.raw.dripping);
                }

                player.start();
                player.setLooping(true);
                imageView.startAnimation(shake);
                splashscreeny.playAnimation();
            } else {
                loopImageView.setVisibility(View.GONE);
                loopButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_loop, 0, 0, 0);
                loopButton.setText(R.string.start_loop);
                loopStatus = false;
                player.stop();
                player.release();
                imageView.clearAnimation();
                splashscreeny.pauseAnimation();
                splashscreeny.setVisibility(View.GONE);

            }


//            if (!loopPlayer.isPlaying()){
//                loopPlayer.start();
//                    loopPlayer.setLooping(true);
//                    loopButton.setText(R.string.stop_loop);
//                loopButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_stop, 0, 0, 0);
//
//            }else {
//                loopPlayer.pause();
//                //loopPlayer.release();
//                loopButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_loop, 0, 0, 0);
//                loopButton.setText(R.string.start_loop);
//
//            }
        });
        ImageView home = findViewById(R.id.home_btn);
        home.setOnClickListener(view -> {
            finish();
        });
        loopImageView.setOnClickListener(v -> Toast.makeText(FartActivity.this, R.string.stop_loop_first, Toast.LENGTH_SHORT).show());
        imageView.setOnTouchListener((v, event) -> {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                splashscreeny.setVisibility(View.VISIBLE);
                if (soundSelect.equals("bike")) {
                    player = MediaPlayer.create(FartActivity.this, R.raw.bubbles);
                } else if (soundSelect.equals("police")) {
                    player = MediaPlayer.create(FartActivity.this, R.raw.poop);
                } else if (soundSelect.equals("ambulance")) {
                    player = MediaPlayer.create(FartActivity.this, R.raw.wet);
                } else {
                    player = MediaPlayer.create(FartActivity.this, R.raw.dripping);
                }

                player.start();
                player.setLooping(true);
                imageView.startAnimation(shake);
                splashscreeny.playAnimation();

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                player.stop();
                player.release();
                imageView.clearAnimation();
                splashscreeny.pauseAnimation();
                splashscreeny.setVisibility(View.GONE);


            }
            return true;

        });

    }

    @Override
    public void onBackPressed() {
        if (loopStatus) {
            cleanUpMediaPlayer();
        }
        super.onBackPressed();
    }

    private void initControls() {
        loopPlayer = MediaPlayer.create(FartActivity.this, R.raw.bubbles);

        CardView card1 = findViewById(R.id.sound1);
        CardView card2 = findViewById(R.id.sound2);
        CardView card3 = findViewById(R.id.sound3);
        CardView card4 = findViewById(R.id.sound4);

        TextView text1 = findViewById(R.id.text_sound1);
        TextView text2 = findViewById(R.id.text_sound2);
        TextView text3 = findViewById(R.id.text_sound3);
        TextView text4 = findViewById(R.id.text_sound4);

        text1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_down);
        imageView.setBackgroundResource(R.drawable.fart1);
        soundSelect = "bike";
        card1.setOnClickListener(v -> {
            if (!loopStatus) {

                imageView.setBackgroundResource(R.drawable.fart1);
                loopPlayer = MediaPlayer.create(FartActivity.this, R.raw.bubbles);
                text1.setText("");
                text2.setText(R.string.poop);
                text3.setText(R.string.wet_fart);
                text4.setText(R.string.dripping);
                text1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_down);
                text2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                text3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                text4.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                soundSelect = "bike";

            } else {
                Toast.makeText(this, R.string.stop_loop_first, Toast.LENGTH_SHORT).show();
            }

        });
        card2.setOnClickListener(v -> {
            if (!loopStatus) {

                imageView.setBackgroundResource(R.drawable.fart2);
                loopPlayer = MediaPlayer.create(FartActivity.this, R.raw.poop);
                text1.setText(R.string.bubbles);
                text2.setText("");
                text3.setText(R.string.wet_fart);
                text4.setText(R.string.dripping);
                text1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                text2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_down);
                text3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                text4.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                soundSelect = "police";
            } else {
                Toast.makeText(this, R.string.stop_loop_first, Toast.LENGTH_SHORT).show();
            }


        });
        card3.setOnClickListener(v -> {
            if (!loopStatus) {

                imageView.setBackgroundResource(R.drawable.fart3);
                loopPlayer = MediaPlayer.create(FartActivity.this, R.raw.wet);
                text1.setText(R.string.bubbles);
                text2.setText(R.string.poop);
                text3.setText("");
                text4.setText(R.string.dripping);
                text1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                text2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                text3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_down);
                text4.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                soundSelect = "ambulance";
            } else {
                Toast.makeText(this, R.string.stop_loop_first, Toast.LENGTH_SHORT).show();

            }

        });
        card4.setOnClickListener(v -> {
            if (!loopStatus) {

                imageView.setBackgroundResource(R.drawable.fart4);
                loopPlayer = MediaPlayer.create(FartActivity.this, R.raw.dripping);
                text1.setText(R.string.bubbles);
                text2.setText(R.string.poop);
                text3.setText(R.string.wet_fart);
                text4.setText("");
                text1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                text2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                text3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                text4.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_down);
                soundSelect = "truck";
            } else {
                Toast.makeText(this, R.string.stop_loop_first, Toast.LENGTH_SHORT).show();

            }

        });

        ImageView low = findViewById(R.id.volumeLow);
        low.setOnClickListener(v -> {
            int index = volumeSeekbar.getProgress();
            volumeSeekbar.setProgress(index - 1);
        });
        ImageView high = findViewById(R.id.volumeUp);
        high.setOnClickListener(v -> {
            int index = volumeSeekbar.getProgress();
            volumeSeekbar.setProgress(index + 1);
        });
        try {
            volumeSeekbar = findViewById(R.id.seekbar1);
            audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

            volumeSeekbar.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeSeekbar.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));
            volumeSeekbar.setMax(20);
            volumeSeekbar.setProgress(15);
            int pro = volumeSeekbar.getProgress();

            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, pro, 0);

            volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            // mediaVlmSeekBar = (SeekBar) findViewById(R.id.seekBar1);
            int index = volumeSeekbar.getProgress();
            volumeSeekbar.setProgress(index + 1);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            int index = volumeSeekbar.getProgress();
            volumeSeekbar.setProgress(index - 1);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void cleanUpMediaPlayer() {
        if (player != null) {
            if (player.isPlaying()) {
                player.stop();
            }
            player.release();
        }
    }

    private void loadBanner() {
        AdView adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.BannerAd));
        frameLayout.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);
        adView.loadAd(adRequest);
    }
}